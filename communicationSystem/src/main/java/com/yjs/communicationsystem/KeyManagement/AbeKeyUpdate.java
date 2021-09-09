package com.yjs.communicationsystem.KeyManagement;

import com.yjs.communicationsystem.cpabeApi.cpabe.Cpabe;
import com.yjs.communicationsystem.ECDH.IoTEcdhUtil;
import com.yjs.communicationsystem.IPFS.util;
import com.yjs.communicationsystem.fabricUtil.Registration.Register;
import com.yjs.communicationsystem.fabricUtil.Verification.verification;
import com.webank.weevent.client.IWeEventClient;
import com.webank.weevent.client.SendResult;
import com.webank.weevent.client.WeEvent;
import io.ipfs.api.IPFS;

import java.util.*;

public class AbeKeyUpdate {

    public static SendResult deviceAbeKeyUpdate(String deviceHash, String channelName, String chaincodeName, IPFS ipfs, IWeEventClient client, String[] deviceAttribute, int keyUpdateRounds) throws Exception {

        Register res = verification.queryDeviceInfo(deviceHash, chaincodeName, channelName);
        //将string类型的设备ECDH public key转化为字节数组类型
        byte[] deviceEcdhPubicKey = Base64.getDecoder().decode(res.getDeviceEcdhPub());

        //定义ABE public parameter和master key文件路径
        String pubParameterFile = "./cpabeKey/cpabePublicParameter/publicParameter";
        String masterKeyFile = "./cpabeKey/cpabePrivateKey/masterKey";

        //将key update的轮数添加到设备属性中
        String rounds = Integer.toString(keyUpdateRounds);
        String updateRounds = "keyUpdateRounds:" + rounds;
        List<String> list=new ArrayList<>();
        list.add(updateRounds);
        //2.数组and集合add到titleList中
        List<String> attributeList = new ArrayList<String>();
        //数组转换list
        attributeList.addAll(Arrays.asList(deviceAttribute));
        //list添加到titleList中
        attributeList.addAll(list);
        //3.titleList集合转换title数组
        String[] newAttributeArray= attributeList.toArray(new String[attributeList.size()]);

        //调用cpabe api的keygen function
        byte[] deviceAbeKey = Cpabe.keygen(pubParameterFile, masterKeyFile, newAttributeArray);
        //返回的结果包括group owner的ECDH公钥以及利用secret key加密的设备CPABE key
        String[] cpAbeResult = IoTEcdhUtil.IoTEcdhEncrypt(deviceAbeKey, deviceEcdhPubicKey);

        String topicName = "batchKeyUpdate";

        Map<String, String> ext = new HashMap<>();
        //将唯一的hash值作为识别设备的身份
        ext.put("weevent-deviceHash", deviceHash);
        //group owner ecdh key
        ext.put("weevent-ecdhKey", cpAbeResult[0]);

        //创建IPFS实例，将设备的cpabe key传上去IPFS上，并返回唯一的hash值
        byte[] deviceCpAbeKey = Base64.getDecoder().decode(cpAbeResult[1]);
        String AbeKey_hash = util.IpfsAdd(ipfs, deviceCpAbeKey);
//                System.out.println(AbeKey_hash);
        //将hash值作为content借助weEvent传输
        ext.put("weevent-abeKeyHash", AbeKey_hash);
        WeEvent weEvent = new WeEvent(topicName, "device's updated abe key and device owner ecdh key".getBytes(), ext);
        SendResult sendResult = client.publish(weEvent);
        return sendResult;
    }

}
