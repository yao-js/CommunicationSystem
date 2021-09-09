package com.webank.weevent.broker.sample;


import java.util.HashMap;
import java.util.Map;

import com.webank.weevent.client.BrokerException;
import com.webank.weevent.client.IWeEventClient;
import com.webank.weevent.client.SendResult;
import com.webank.weevent.client.WeEvent;
public class JavaSDK2 {
    public static void main(String[] args) {
        System.out.println("This is WeEvent Java SDK sample.");
        try {
            // get client
            IWeEventClient client = IWeEventClient.builder().brokerUrl("http://localhost:7000/weevent-broker").userName("user").password("123456").groupId("mychannel").build();
            Map<String, String> ext = new HashMap<>();

//            System.out.println(client.state("com.weevent.test"));
//            // ensure topic exist
            String topicName = "test";
//            client.open(topicName);
//
            // publish "hello world" to topic
//            ext.put("weevent-aaa", "ddd");
            WeEvent weEvent = new WeEvent(topicName, "shadiao wulaoban".getBytes(), ext);

            SendResult sendResult = client.publish(weEvent);
            System.out.println(sendResult);
//
//            // subscribe topic
//            String subscriptionId = client.subscribe(topicName, WeEvent.OFFSET_LAST, ext, new IWeEventClient.EventListener() {
//                @Override
//                public void onEvent(WeEvent event) {
//                    System.out.println("received event: " + event);
//                }
//
//                @Override
//                public void onException(Throwable e) {
//
//                }
//            });

            // unSubscribe topic
//            client.unSubscribe(subscriptionId);
        } catch (BrokerException e) {
            e.printStackTrace();
        }
    }
}
