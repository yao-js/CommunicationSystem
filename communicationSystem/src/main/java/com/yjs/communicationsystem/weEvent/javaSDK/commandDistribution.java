package com.yjs.communicationsystem.weEvent.javaSDK;

import com.webank.weevent.client.IWeEventClient;
import com.webank.weevent.client.SendResult;

public class commandDistribution {
    public static void main(String[] args) throws Exception {
//        IWeEventClient client = IWeEventClient.builder().brokerUrl("http://localhost:7000/weevent-broker").groupId("mychannel").build();
        IWeEventClient client = IWeEventClient.builder().brokerUrl("http://localhost:7000/weevent-broker").userName("user").password("123456").groupId("mychannel").build();
//        IWeEventClient client = IWeEventClient.builder().brokerUrl("http://47.242.5.78:7000/weevent-broker").groupId("mychannel").build();
        String hash = "f5a9a8827a898c347b4e0d35b083ccaefc0343665eac72ab54c16323e1709d1d";
        String channelName = "mychannel";
        String chaincodeName = "IoT";
        String commandFile = "commandFile/input.txt";
        String policy = "sn:student2 cn:student2 uid:student2 3of3";
        String topicName = "CommandDistribution";
        SendResult sendResult = JavaSDK.encryptMessageDistribution(client, topicName, policy,commandFile, hash, chaincodeName, channelName);
        System.out.println(sendResult);
//        if(sendResult != null) return;
    }
}

