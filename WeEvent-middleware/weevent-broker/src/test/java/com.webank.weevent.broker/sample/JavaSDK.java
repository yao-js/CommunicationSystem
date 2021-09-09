package com.webank.weevent.broker.sample;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.webank.weevent.client.BrokerException;
import com.webank.weevent.client.IWeEventClient;
import com.webank.weevent.client.SendResult;
import com.webank.weevent.client.WeEvent;

/**
 * Sample of Java SDK.
 *
 * @author matthewliu
 * @since 2019/04/07
 */
public class JavaSDK {


    public static void main(String[] args) throws BrokerException {

        System.out.println("This is WeEvent Java SDK sample.");
        try {
            // get client
            IWeEventClient client = IWeEventClient.builder().brokerUrl("http://localhost:7000/weevent-broker").userName("user").password("123456").groupId("mychannel").build();
//            IWeEventClient client = IWeEventClient.builder().brokerUrl("http://localhost:7000/weevent-broker").groupId("mychannel").build();
            Map<String, String> ext = new HashMap<>();
//            System.out.println(client.state("com.weevent.test"));
//            // ensure topic exist
            String topicName = "test";
//            client.open(topicName);
//
////
            // subscribe topic
            String subscriptionId = client.subscribe(topicName, WeEvent.OFFSET_LAST, ext, new IWeEventClient.EventListener() {
                @Override
                public void onEvent(WeEvent event) {
                    String Hash = event.getExtensions().get("weevent-deviceHash");
                    String content = new String(event.getContent());
                    System.out.println("received event: " + Hash);
                    System.out.println("received content: " + content);
                }
                @Override
                public void onException(Throwable e) {

                }
            });

//            // publish "hello world" to topic
//            ext.put("aaa", "ddd");
//            ext.put("weevent-deviceHash", "f5a9a8827a898c347b4e0d35b083ccaefc0343665eac72ab54c16323e1709d1d");
//            WeEvent weEvent = new WeEvent(topicName, "hello WeEvent".getBytes(StandardCharsets.UTF_8),ext);
//            SendResult sendResult = client.publish(weEvent);
//            System.out.println(sendResult);
//
            // unSubscribe topic
//            Thread.currentThread().sleep(1000);
//            client.unSubscribe(subscriptionId);
        } catch (BrokerException e) {
            e.printStackTrace();
        }
    }
}
