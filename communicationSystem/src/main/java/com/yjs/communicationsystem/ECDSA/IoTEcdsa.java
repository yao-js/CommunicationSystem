package com.yjs.communicationsystem.ECDSA;

import com.yjs.communicationsystem.cpabeApi.ellipticcurve.Ecdsa;
import com.yjs.communicationsystem.cpabeApi.ellipticcurve.PrivateKey;
import com.yjs.communicationsystem.cpabeApi.ellipticcurve.PublicKey;
import com.yjs.communicationsystem.cpabeApi.ellipticcurve.Signature;

import java.io.*;
import java.math.BigInteger;

import static com.yjs.communicationsystem.ECDSA.EcdsaJava.*;

public class IoTEcdsa {

    static {

        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    //公钥
    private static final String PUBLIC_KEY = "ECPublicKey";

    //私钥
    private static final String PRIVATE_KEY = "ECPrivateKey";

    /* read byte[] from inputfile */
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

    public static Signature sign(String message, PrivateKey DeviceOwnerPrivateKey){
        return Ecdsa.sign(message, DeviceOwnerPrivateKey);
    }


    public static String[] signature(byte[] message, String DeviceOwnerECDSAPrivateKeyFileName, String DeviceOwnerECDSAPublicKeyFileName) throws Exception {
        PrivateKey privateKey;
        PublicKey publicKey;
        String deviceOwnerEcdsaPublicKey;
        File ECDSAPriFile = new File(DeviceOwnerECDSAPrivateKeyFileName);
        File ECDSAPubFile = new File(DeviceOwnerECDSAPublicKeyFileName);

        //判断ECDSA公私钥是否已经存在本地，若存在则在文件中获取，若不存在则new一对公私钥。
        if(!ECDSAPriFile.exists()){
            // Generate Keys
            //设备拥有者生成公私钥
            privateKey = new PrivateKey();
            String deviceOwnerEcdsaPrivateKey = privateKey.toPem();
            spitFile(DeviceOwnerECDSAPrivateKeyFileName,deviceOwnerEcdsaPrivateKey.getBytes());
        }else {
            privateKey = new PrivateKey().fromPem(new String(suckFile(DeviceOwnerECDSAPrivateKeyFileName)));
        }

        if(!ECDSAPubFile.exists()){
            //从私钥中获取公钥
            publicKey = privateKey.publicKey();
            deviceOwnerEcdsaPublicKey = publicKey.toPem();
            spitFile(DeviceOwnerECDSAPublicKeyFileName,deviceOwnerEcdsaPublicKey.getBytes());
        }else {
            publicKey = new PrivateKey().publicKey().fromPem(new String(suckFile(DeviceOwnerECDSAPublicKeyFileName)));
            deviceOwnerEcdsaPublicKey = publicKey.toPem();
        }

        String deviceMessage = new String(message);
        //对设备证书进行签名
        Signature signature = IoTEcdsa.sign(deviceMessage, privateKey);
        String rPubSign = signature.r.toString();
        String sPubSign = signature.s.toString();

        String[] deviceSignCert = {deviceOwnerEcdsaPublicKey,deviceMessage,rPubSign,sPubSign};
        return deviceSignCert;
    }

    public static String[] signature(String DeviceCertFile) throws Exception{
        PrivateKey privateKey;
        PublicKey publicKey;
        String deviceOwnerEcdsaPublicKey;
        File ECDSAPriFile = new File("./deviceOwnerEcdsaKey/DeviceOwnerPrivateKey.pem");
        File ECDSAPubFile = new File("./deviceOwnerEcdsaKey/DeviceOwnerPublicKey.pem");

        //判断ECDSA公私钥是否已经存在本地，若存在则在文件中获取，若不存在则new一对公私钥。
        if(!ECDSAPriFile.exists()){
            // Generate Keys
            //设备拥有者生成公私钥
            privateKey = new PrivateKey();
            String deviceOwnerEcdsaPrivateKey = privateKey.toPem();
            spitFile("./deviceOwnerEcdsaKey/DeviceOwnerPrivateKey.pem",deviceOwnerEcdsaPrivateKey.getBytes());
        }else {
            privateKey = new PrivateKey().fromPem(new String(suckFile("./deviceOwnerEcdsaKey/DeviceOwnerPrivateKey.pem")));
        }

        if(!ECDSAPubFile.exists()){
            //从私钥中获取公钥
            publicKey = privateKey.publicKey();
            deviceOwnerEcdsaPublicKey = publicKey.toPem();
            spitFile("./deviceOwnerEcdsaKey/DeviceOwnerPublicKey.pem",deviceOwnerEcdsaPublicKey.getBytes());
        }else {
            publicKey = new PrivateKey().publicKey().fromPem(new String(suckFile("./deviceOwnerEcdsaKey/DeviceOwnerPublicKey.pem")));
            deviceOwnerEcdsaPublicKey = publicKey.toPem();
        }

        //将设备证书文件转化为字节数组类型
        byte[] dvCert = suckFile(DeviceCertFile);
        String deviceCert = new String(dvCert);

        //对设备证书进行签名
        Signature signature = IoTEcdsa.sign(deviceCert, privateKey);
        String rPubSign = signature.r.toString();
        String sPubSign = signature.s.toString();

        String[] deviceSignCert = {deviceOwnerEcdsaPublicKey,deviceCert,rPubSign,sPubSign};
        return deviceSignCert;

    }

    public static String[] signaturePro(String DeviceCertFile) throws Exception {
        byte[] priv = suckFile("deviceOwnerEcdsaKey/ecdsa_priv");
        java.security.PrivateKey pri = bytesToPrivateKey(priv);
        String priStringHex = getPrivateKeyAsHex(pri);
        //将设备证书文件转化为字节数组类型
        byte[] dvCert = suckFile(DeviceCertFile);
        String deviceCert = new String(dvCert);
        String[] res = signMsg(deviceCert, pri);
        String[] deviceSignCert = {priStringHex, deviceCert, res[0], res[1]};
        return deviceSignCert;
    }

    public static boolean verify(String DevicePublicKey, String rSignMessage, String sSignMessage, String DeviceOwnerPublicKey){

        BigInteger rSign = new BigInteger(rSignMessage);
        BigInteger sSign = new BigInteger(sSignMessage);
        Signature signMessage = new Signature(rSign,sSign);

        //获取设备拥有者的ECDSA公钥
        PublicKey doPub = PublicKey.fromPem(DeviceOwnerPublicKey);
        //利用ECDSA签名算法验证签名的有效性
        boolean result = Ecdsa.verify(DevicePublicKey, signMessage, doPub);

        return result;
    }



    public static void main(String[] args) throws Exception {
        byte[] res = suckFile("Device/DeviceCert/certificate.pem");
        signature(res, "deviceOwnerEcdsaKey/ec-pri.pem", "deviceOwnerEcdsaKey/ec-pub.pem");
    }
}
