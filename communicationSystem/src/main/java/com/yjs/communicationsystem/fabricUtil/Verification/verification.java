//package com.yjs.communicationsystem.fabricUtil.Verification;
//
//import com.yjs.communicationsystem.ECDSA.IoTEcdsa;
//import com.yjs.communicationsystem.fabricUtil.Registration.Register;
//import net.sf.json.JSONObject;
//import org.hyperledger.fabric.gateway.*;
//
//import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//public class verification {
//
//    //根据设备hash值查询存储在区块链上的设备的信息
//    public static Register queryDeviceInfo(String hash, String chaincodeName, String channelName) throws ContractException, IOException {
//        //调用chaincode
//        Path walletPath = Paths.get("/Users/yiukingsing/Desktop/paperwork/Hyperledgerfabric/fabric-samples-release-1.4/PaperExperiment/wallet");
//        Wallet wallet = Wallet.createFileSystemWallet(walletPath);
//        // load a CCP
//        Path networkConfigPath = Paths.get("/Users/yiukingsing/Desktop/paperwork/Hyperledgerfabric/fabric-samples-release-1.4/PaperExperiment/connection-org1.yaml");
//        Gateway.Builder builder = Gateway.createBuilder();
//        builder.identity(wallet, "user2").networkConfig(networkConfigPath).discovery(true);
//
//        // create a gateway connection
//        try (Gateway gateway = builder.connect()) {
//            // get the network and contract
//            Network network = gateway.getNetwork(channelName);
//            //set up the chaincode name
//            Contract contract = network.getContract(chaincodeName);
//
//            byte[] deviceInfo = contract.evaluateTransaction("queryRegistration", hash);
////            System.out.println(new String((result)));
//            //1、使用JSONObject
//            JSONObject jsonObject = JSONObject.fromObject(new String(deviceInfo));
//            Register res = (Register) JSONObject.toBean(jsonObject, Register.class);
//            return res;
//        }
//    }
//
//    public static boolean verify(String hash, String chaincodeName, String channelName) throws Exception {
//        Register res = queryDeviceInfo(hash, chaincodeName, channelName);
////            BigInteger rSign = new BigInteger(res.getrPubSign());
////            BigInteger sSign = new BigInteger(res.getsPubSign());
//
//        boolean result = IoTEcdsa.verify(res.getDevicePub(), res.getrPubSign(), res.getsPubSign(), res.getDeviceOwnerPub());
//        return result;
//    }
//
//    //跨组通信的时候，需要请求fabric获取对应组的public parameter来加密（每个group owner定期向fabric请求更新其他组的public parameter）
//
//    public static void main(String[] args) throws Exception {
//        long startTime=System.currentTimeMillis(); //获取开始时间
//        boolean result = verify("e67d66080bc90690230758418f3094c2bd6e66019a4d041dcd17fa307daafdfc", "IoT","mychannel");
////        boolean result = verify("ef2e16680fd0d6ac3bd98788e020d6cbfce5eae89abece2d9ff070858afb0bbf", "IoT","mychannel");
////        Register res = queryDeviceInfo("f5a9a8827a898c347b4e0d35b083ccaefc0343665eac72ab54c16323e1709d1d", "IoT", "mychannel");
////        System.out.println(res());
//        System.out.println(result);
//        long endTime=System.currentTimeMillis(); //获取结束时间
//        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
//    }
//
//
//
//
//
//
//
//}
//
//
