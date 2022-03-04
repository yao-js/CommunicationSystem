//package com.yjs.communicationsystem.fabricUtil.Registration;
//
//import org.hyperledger.fabric.gateway.Contract;
//import org.hyperledger.fabric.gateway.Gateway;
//import org.hyperledger.fabric.gateway.Network;
//import org.hyperledger.fabric.gateway.Wallet;
//
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import static com.yjs.communicationsystem.ECDH.IoTEcdhUtil.ecdhKeyGeneration;
//import static com.yjs.communicationsystem.ECDSA.IoTEcdsa.signaturePro;
//
//public class registerClientInfo {
//    public static void main(String[] args) throws Exception {
////        String hash = "f5a9a8827a898c347b4e0d35b083ccaefc0343665eac72ab54c16323e1709d1d";
////
////        Boolean res = verification.verify(hash, "IoT", "mychannel");
////
////        System.out.println(res);
//        long startTime=System.currentTimeMillis(); //获取开始时间
//
//        // Load a file system based wallet for managing identities.
//        Path walletPath = Paths.get("wallet");
//        Wallet wallet = Wallet.createFileSystemWallet(walletPath);
//
//        // load a CCP
//        Path networkConfigPath = Paths.get("connection-org1.yaml");
//
//        Gateway.Builder builder = Gateway.createBuilder();
//        builder.identity(wallet, "user2").networkConfig(networkConfigPath).discovery(true);
//
//        // create a gateway connection
//        try (Gateway gateway = builder.connect()) {
////
//            // get the network and contract
//            Network network = gateway.getNetwork("mychannel");
//            Contract contract = network.getContract("register");
////
//            //register device "yjs-MacBookPro"
////            String[] deviceSignCert = signature("Device/DeviceCert/certificate.pem");
////            String[] deviceSignCert = signature("Device/DeviceCert/certificate01.pem");
//            String[] deviceSignCert = signaturePro("Device/DeviceCert/certificate02.pem");
////
//            String deviceEcdhPubicKey = ecdhKeyGeneration();
//            String deviceID = "lz-MacOs";
////            String deviceOwnerEcdsaPublicKey = deviceSignCert[0];
//            String priStringHex = deviceSignCert[0];
//            String deviceCert = deviceSignCert[1];
//            String rPubSign = deviceSignCert[2];
//            String sPubSign = deviceSignCert[3];
//
//            String[] a = new String[]{"a", "b", "c"};
//            contract.submitTransaction("a", a);
////            byte[] hash = contract.submitTransaction("createRegistration", deviceID,
////                    priStringHex, rPubSign, sPubSign, deviceCert, deviceEcdhPubicKey);
//            byte[] result = contract.submitTransaction("verifyDeviceIdentity",
//                    "20cddc4157155545c1d22fe4242f0e92fd98eac3754612c5beb59e3d5eb39641");
//            boolean boolResult = Boolean.valueOf(new String(result)).booleanValue();
//            System.out.println(boolResult);
////            byte[] hash = contract.submitTransaction("TestingRegistration", deviceID,
////                    deviceOwnerEcdsaPublicKey, rPubSign, sPubSign, deviceEcdhPubicKey);
////            byte[] hash = contract.submitTransaction("createRegistration", "deviceID",
////                    "deviceOwnerEcdsaPublicKey", "rPubSign", "sPubSign", "abcdefg", "deviceEcdhPubicKey");
//
//            //record device's hash into local list
////            FileWriter fw=new FileWriter("DeviceHashList/input.txt",true);
////            fw.write(new String(hash)+"\r\n");
////            fw.close();
//
//
////            System.out.println(new String(hash));
//            long endTime=System.currentTimeMillis(); //获取结束时间
//            System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
////
//        }
//    }
//}
