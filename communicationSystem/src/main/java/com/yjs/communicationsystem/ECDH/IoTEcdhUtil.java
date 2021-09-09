package com.yjs.communicationsystem.ECDH;

import java.io.*;
import java.util.Base64;
import java.util.Map;

public class IoTEcdhUtil {

    public static byte[] suckFile(String inputfile) throws IOException {
        InputStream is = new FileInputStream(inputfile);
        int size = is.available();
        byte[] content = new byte[size];

        is.read(content);

        is.close();
        return content;
    }

    /* write byte[] into outputfile */
    public static void spitFile(String outputfile, byte[] b) throws IOException {
        OutputStream os = new FileOutputStream(outputfile);
        os.write(b);

        os.close();
    }

    public static String ecdhKeyGeneration() throws Exception {

        Map<String, Object> Device_key = util.initKey();
        String deviceEcdhPubicKey;
        byte[] DeviceEcdhPriKey;
        byte[] DeviceEcdhPubKey;

        File PrivateKeyfile = new File("./Device/DeviceEcdhKey/EcdhPrivateKey");
        File PublicKeyfile = new File("./Device/DeviceEcdhKey/EcdhPublicKey");

        //判断设备的ECDH公私钥是否已经存在本地，若存在则在文件中获取，若不存在则new一对公私钥。
        //将设备的ECDH私钥存到文件夹中
        if(!PrivateKeyfile.exists()){
            Device_key = util.initKey();
            //Device ECDH key
            DeviceEcdhPriKey = util.getPrivateKey(Device_key);
            spitFile("./Device/DeviceEcdhKey/EcdhPrivateKey", DeviceEcdhPriKey);
        }

        if(!PublicKeyfile.exists()){
            DeviceEcdhPubKey = util.getPublicKey(Device_key);

            spitFile("./Device/DeviceEcdhKey/EcdhPublicKey", DeviceEcdhPubKey);
        }else{
            DeviceEcdhPubKey = suckFile("./Device/DeviceEcdhKey/EcdhPublicKey");
        }

        //将设备ecdh公钥转化为string类型
        deviceEcdhPubicKey = Base64.getEncoder().encodeToString(DeviceEcdhPubKey);
        return deviceEcdhPubicKey;
    }

    public static String[] IoTEcdhEncrypt(byte[] deviceCpAbeKey, byte[] deviceEcdhPubicKey) throws Exception {

//        String deviceCpAbeKey = Base64.getEncoder().encodeToString(cpAbeKey);
        Map<String, Object> groupOwnerKey = util.initKey();
        //group owner的ECDH key
        byte[] groupOwnerEcdhPriKey;
        byte[] groupOwnerEcdhPubKey;
        File DeviceOwnerECDHPublicKey = new File("/Users/yiukingsing/Desktop/paperwork/Hyperledgerfabric/fabric-samples-release-1.4/PaperExperiment/GroupOwnerECDHkey/GroupOwnerEcdhPublicKey");
        File DeviceOwnerECDHPrivateKey = new File("/Users/yiukingsing/Desktop/paperwork/Hyperledgerfabric/fabric-samples-release-1.4/PaperExperiment/GroupOwnerECDHkey/GroupOwnerEcdhPrivateKey");

        //判断group owner的ECDH公私钥是否已经存在本地，若存在则在文件中获取，若不存在则new一对公私钥。
        if(!DeviceOwnerECDHPrivateKey.exists()){
            groupOwnerEcdhPriKey = util.getPrivateKey(groupOwnerKey);
            spitFile("/Users/yiukingsing/Desktop/paperwork/Hyperledgerfabric/fabric-samples-release-1.4/PaperExperiment/GroupOwnerECDHkey/GroupOwnerEcdhPrivateKey", groupOwnerEcdhPriKey);
        }else{
            groupOwnerEcdhPriKey = suckFile("/Users/yiukingsing/Desktop/paperwork/Hyperledgerfabric/fabric-samples-release-1.4/PaperExperiment/GroupOwnerECDHkey/GroupOwnerEcdhPrivateKey");
        }

        if(!DeviceOwnerECDHPublicKey.exists()){
            groupOwnerEcdhPubKey = util.getPublicKey(groupOwnerKey);
            spitFile("/Users/yiukingsing/Desktop/paperwork/Hyperledgerfabric/fabric-samples-release-1.4/PaperExperiment/GroupOwnerECDHkey/GroupOwnerEcdhPublicKey", groupOwnerEcdhPubKey);
        }else{
            groupOwnerEcdhPubKey = suckFile("/Users/yiukingsing/Desktop/paperwork/Hyperledgerfabric/fabric-samples-release-1.4/PaperExperiment/GroupOwnerECDHkey/GroupOwnerEcdhPublicKey");
        }

        //将group owner的public key转化为String类型
        String groupOwnerEcdhPublicKey = Base64.getEncoder().encodeToString(groupOwnerEcdhPubKey);

        //利用设备的ECDH公钥以及group owner的私钥来生成secret key
        byte[] secretKey =  util.getSecretKey(deviceEcdhPubicKey, groupOwnerEcdhPriKey);
        //利用secret key加密设备的CPABE key
        byte[] encryptedCpAbeKey = util.encrypt(deviceCpAbeKey, secretKey);
        String deviceEncryptedCpAbeKey = Base64.getEncoder().encodeToString(encryptedCpAbeKey);

        String[] result = {groupOwnerEcdhPublicKey,deviceEncryptedCpAbeKey};


        return result;

    }

    public static byte[] IoTEcdhDecrypt(byte[] deviceEncryptedCpAbeKey, String groupOwnerEcdhPublicKey, byte[] deviceEcdhPrivateKey) throws Exception {
        //将字符串类型转化为字节数组
        byte[] groupOwnerEcdhPubkey = Base64.getDecoder().decode(groupOwnerEcdhPublicKey);
//        byte[] deviceEncCpAbeKey = Base64.getDecoder().decode(deviceEncryptedCpAbeKey);
        //利用group owner的公钥以及设备的私钥生成secret key，用于ECDH解密
        byte[] secretKey = util.getSecretKey(groupOwnerEcdhPubkey, deviceEcdhPrivateKey);

        byte[] decryptMessage = util.decrypt(deviceEncryptedCpAbeKey, secretKey);
        return decryptMessage;
    }
}
