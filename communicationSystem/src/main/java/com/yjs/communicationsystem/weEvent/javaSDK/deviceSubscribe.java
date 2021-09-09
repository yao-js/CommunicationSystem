package com.yjs.communicationsystem.weEvent.javaSDK;

import com.webank.weevent.client.BrokerException;
import com.webank.weevent.client.IWeEventClient;

import java.io.IOException;

public class deviceSubscribe {
    public static void main(String[] args) throws BrokerException, IOException {
//        IWeEventClient client = IWeEventClient.builder().brokerUrl("http://47.242.5.78:7000/weevent-broker").groupId("mychannel").timeout(100000).build();
//        IWeEventClient client = IWeEventClient.builder().brokerUrl("http://localhost:7000/weevent-broker").groupId("mychannel").timeout(100000).build();
//        IWeEventClient client = IWeEventClient.builder().brokerUrl("http://8.134.77.11:7000/weevent-broker").userName("user").password("123456").groupId("mychannel").build();
        IWeEventClient client = IWeEventClient.builder().brokerUrl("http://localhost:7000/weevent-broker").userName("user").password("123456").groupId("mychannel").build();
//        String topicName = "keyDistribution";
        String topicName = "CommandDistribution";
        String deviceHash = "e67d66080bc90690230758418f3094c2bd6e66019a4d041dcd17fa307daafdfc";
        // ensure topic exist
//        client.open(topicName);
        JavaSDK.deviceSubscribe(client, topicName,deviceHash);
    }
}