package com.yjs.communicationsystem.weEvent.javaSDK;

import com.yjs.communicationsystem.cpabeApi.cpabe.Cpabe;
import com.yjs.communicationsystem.ECDH.IoTEcdhUtil;
import com.yjs.communicationsystem.IPFS.util;
import com.yjs.communicationsystem.fabricUtil.Registration.Register;
import com.yjs.communicationsystem.fabricUtil.Verification.verification;
import com.webank.weevent.client.BrokerException;
import com.webank.weevent.client.IWeEventClient;
import com.webank.weevent.client.SendResult;
import com.webank.weevent.client.WeEvent;
import io.ipfs.api.IPFS;
import lombok.SneakyThrows;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Sample of Java SDK.
 *
 * @author matthewliu
 * @since 2019/04/07
 */
public class JavaSDK {

    public static SendResult keyDistribution(IWeEventClient client, String hash, String[] deviceAttribute, String chaincodeName, String channelName) throws Exception {

        SendResult sendResult = null;
        boolean result = verification.verify(hash, chaincodeName, channelName);
        System.out.println(result);
        //当验证设备的数字证书签名通过后，执行CP-ABE key的distribution
        if (result == true) {
            try {

//                Date date = new Date();
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(date);// 设置起时间
//                // 设置有效期，如增加一年
//                cal.add(Calendar.YEAR, 1);
//                // 设置时间格式为：年-月-日 时：分：秒
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String validTIme = format.format(cal.getTime());
//                //设置device key的有效时间，调用chaincode中addTimeStamp function
//                TimeStampUtil.addTimeStamp(channelName, chaincodeName, hash, validTIme);

                Map<String, String> ext = new HashMap<>();
                Register res = verification.queryDeviceInfo(hash, chaincodeName, channelName);
                //将string类型的设备ECDH public key转化为字节数组类型
                byte[] deviceEcdhPubicKey = Base64.getDecoder().decode(res.getDeviceEcdhPub());

                //定义ABE public parameter和master key文件路径
                String pubParameterFile = "./cpabeKey/cpabePublicParameter/publicParameter";
                String masterKeyFile = "./cpabeKey/cpabePrivateKey/masterKey";
                //调用cpabe api的keygen function
                byte[] deviceAbeKey = Cpabe.keygen(pubParameterFile, masterKeyFile, deviceAttribute);
                //返回的结果包括group owner的ECDH公钥以及利用secret key加密的设备CPABE key
                String[] cpAbeResult = IoTEcdhUtil.IoTEcdhEncrypt(deviceAbeKey, deviceEcdhPubicKey);
                // ensure topic exist
                String topicName = "keyDistribution";
                //将唯一的hash值作为识别设备的身份
                ext.put("weevent-deviceHash", hash);

                ext.put("weevent-ecdhKey", cpAbeResult[0]);

                //创建IPFS实例，将设备的cpabe key传上去IPFS上，并返回唯一的hash值
                IPFS ipfs = new IPFS("/ip4/8.134.77.11/tcp/5001");
                byte[] deviceCpAbeKey = Base64.getDecoder().decode(cpAbeResult[1]);
                String AbeKey_hash = util.IpfsAdd(ipfs, deviceCpAbeKey);
//                System.out.println(AbeKey_hash);
                //将hash值作为content借助weEvent传输
                ext.put("weevent-abeKeyHash", AbeKey_hash);


                WeEvent weEvent = new WeEvent(topicName, "device's abe key and device owner ecdh key".getBytes(), ext);
                sendResult = client.publish(weEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("Verification is failure");
        }
        return sendResult;
    }

    public static SendResult encryptMessageDistribution(IWeEventClient client, String TopicName, String policy, String ManagementCommandFile, String hash, String chaincodeName, String channelName)
            throws Exception {
        //定义public parameter的文件路径
        String pubParameterFile = "./cpabeKey/cpabePublicParameter/publicParameter";
        SendResult sendResult = null;
//        boolean result = verification.verify(hash, chaincodeName, channelName);
//        if(result == true){
        try {
            // get client
            Map<String, String> ext = new HashMap<>();
            // ensure topic exist
            // publish "encrypted management command" to topic
            ext.put("weevent-deviceHash", hash);

            //enc the management command
            byte[][] encInfo = Cpabe.enc(pubParameterFile, policy, ManagementCommandFile);
            byte[] aesBuf = encInfo[0];
            byte[] cphBuf = encInfo[1];

            //将encrypt info利用ipfs hash存储
            IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
            String cphBufIpfsHash = util.IpfsAdd(ipfs,cphBuf);
            String aesBufString = Base64.getEncoder().encodeToString(aesBuf);
//                String cphBufString = Base64.getEncoder().encodeToString(cphBuf);
            //put the encrypted command information into event
            ext.put("weevent-aesBufString", aesBufString);
            ext.put("weevent-cphBufIpfsHash", cphBufIpfsHash);

//                ext.put("weevent-encryptCommand", encryptManagementCommand);

            WeEvent weEvent = new WeEvent(TopicName, "encrypted command".getBytes(), ext);
            sendResult = client.publish(weEvent);
            return sendResult;
        } catch (BrokerException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void deviceSubscribe(IWeEventClient client, String topicName, String DeviceHash) throws BrokerException, IOException {
        Map<String, String> ext = new HashMap<>();
        //记录存入IPFS中的有效期hash值
//        final String[] validTimeIpfsHash = new String[1];
        //设备的ECDH private key
        final byte[] deviceEcdhPriKey = IoTEcdhUtil.suckFile("Device/DeviceEcdhKey/EcdhPrivateKey");
        //定义public parameter的文件路径
        String pubParameterFile = "./cpabeKey/cpabePublicParameter/publicParameter";
//        client.open(topicName);
//        if(client.state(topicName) == null){
//            client.open(topicName);
//        }

        if(topicName == "keyDistribution"){

            String subscriptionId = client.subscribe(topicName, WeEvent.OFFSET_LAST, ext, new IWeEventClient.EventListener() {
                int count = 0;
                @SneakyThrows
                @Override
                public void onEvent(WeEvent event) {
                    count += 1;
                    System.out.println(new String(event.getContent()));
                    String deviceHash = event.getExtensions().get("weevent-deviceHash");
                    if(deviceHash.contentEquals(DeviceHash)){
                        String groupOwnerEcdhKey = event.getExtensions().get("weevent-ecdhKey");
                        //根据传过来的deviceAbeHash在IPFS上get content
                        IPFS ipfs = new IPFS("/ip4/8.134.76.196/tcp/5011");
                        String deviceAbeHash = event.getExtensions().get("weevent-abeKeyHash");
                        byte[] deviceEncAbeKey = util.IpfsGet(ipfs, deviceAbeHash);
                        byte[] deviceAbeKey = IoTEcdhUtil.IoTEcdhDecrypt(deviceEncAbeKey, groupOwnerEcdhKey,deviceEcdhPriKey);
                        System.out.println(deviceAbeKey.length);
//                        System.out.println(count);
                        System.out.println(System.currentTimeMillis());
                        IoTEcdhUtil.spitFile("Device/deviceAbeKey/abeKey01", deviceAbeKey);
                    }
                }
                @Override
                public void onException(Throwable e) {}
            });
        }else if (topicName == "CommandDistribution"){
            String deviceCpAbeKeyFile = "Device/deviceAbeKey/abeKey01";
            String decryptCommandFile = "decryptCommandFile/command.txt";

            String subscriptionId = client.subscribe(topicName, WeEvent.OFFSET_LAST, ext, new IWeEventClient.EventListener() {
                @SneakyThrows
                @Override
                public void onEvent(WeEvent event) {
//                    System.out.println("Ddd");
                    IPFS ipfs = new IPFS("/ip4/8.134.76.196/tcp/5011");
                    Calendar cal = Calendar.getInstance();
                    Date date = cal.getTime();
                    //区分设备的证书唯一hash值
                    String deviceHash = event.getExtensions().get("weevent-deviceHash");
//                    System.out.println(deviceHash.contentEquals(DeviceHash));
                    if(deviceHash.contentEquals(DeviceHash)){
                        System.out.println("event ID is :" + event.getEventId());
                        String aesBufString  = event.getExtensions().get("weevent-aesBufString");
                        String cphBufIpfsHash = event.getExtensions().get("weevent-cphBufIpfsHash");
                        //获取IPFS上的encrypt info
//                      System.out.println(cphBufIpfsHash);
                        byte[] cphBuf = util.IpfsGet(ipfs, cphBufIpfsHash);
                        byte[] aesBuf = Base64.getDecoder().decode(aesBufString);
                        //execute the dec function when the decryption key is valid within the valid time
                        String result = Cpabe.dec(pubParameterFile, deviceCpAbeKeyFile, aesBuf, cphBuf, decryptCommandFile);
                        System.out.println(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(date));
                        System.out.println(result);
                    }
                }
                @Override
                public void onException(Throwable e) {}
            });
        }

    }

    public static void deviceUnsubscribe(String subscriptionId, IWeEventClient client) throws BrokerException {
        client.unSubscribe(subscriptionId);
    }


}



