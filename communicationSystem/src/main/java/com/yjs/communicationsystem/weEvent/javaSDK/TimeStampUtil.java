package com.yjs.communicationsystem.weEvent.javaSDK;

import org.hyperledger.fabric.gateway.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

public class TimeStampUtil {
    public static String addTimeStamp(String channelName, String chaincodeName,String deviceCertHash, String KeyValidTimeStamp) throws InterruptedException, TimeoutException, ContractException, IOException {

        //调用chaincode
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallet.createFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("connection-org1.yaml");
        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "user1").networkConfig(networkConfigPath).discovery(true);

        // create a gateway connection
        try (Gateway gateway = builder.connect()) {
            // get the network and contract
            Network network = gateway.getNetwork(channelName);
            //set up the chaincode name
            Contract contract = network.getContract(chaincodeName);

            byte[] hash = contract.submitTransaction("addTimeStamp", deviceCertHash, KeyValidTimeStamp);
            return new String(hash);
        }
    }

    public static boolean verifyTimeStamp(String channelName, String chaincodeName, String deviceCertHash) throws InterruptedException, TimeoutException, ContractException, IOException {
        //调用chaincode
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallet.createFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("connection-org1.yaml");
        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "user1").networkConfig(networkConfigPath).discovery(true);

        // create a gateway connection
        try (Gateway gateway = builder.connect()) {
            // get the network and contract
            Network network = gateway.getNetwork(channelName);
            //set up the chaincode name
            Contract contract = network.getContract(chaincodeName);

            byte[] hash = contract.submitTransaction("verifyTimeStamp", deviceCertHash);
            return Boolean.parseBoolean(new String(hash));
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException, ContractException {
        long startTime=System.currentTimeMillis(); //获取开始时间
        // Load a file system based wallet for managing identities.
        String deviceCertHash = "f5a9a8827a898c347b4e0d35b083ccaefc0343665eac72ab54c16323e1709d1d";
        String KeyValidTimeStamp = "2030-10-22 21:45:05";
        String KeyCurrentTimeStamp = "2009-01-02 15:04:05";
        String hash = addTimeStamp("mychannel", "IoTchaincode", deviceCertHash, KeyValidTimeStamp);
        System.out.println(hash);
//        boolean result = verifyTimeStamp("mychannel", "IoTchaincode", deviceCertHash, KeyCurrentTimeStamp);
//        System.out.println(result);
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
    }

}
