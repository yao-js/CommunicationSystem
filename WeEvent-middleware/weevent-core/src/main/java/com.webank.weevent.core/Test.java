package com.webank.weevent.core;

import com.webank.weevent.client.*;
import com.webank.weevent.core.IConsumer;
import com.webank.weevent.core.config.FabricConfig;
import com.webank.weevent.core.dto.SubscriptionInfo;
import com.webank.weevent.core.fabric.FabricBroker4Consumer;
import com.webank.weevent.core.fabric.FabricBroker4Producer;
import com.webank.weevent.core.fabric.sdk.FabricDelegate;
import com.webank.weevent.core.task.NotifyTask;
import com.webank.weevent.core.task.Subscription;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.protos.peer.Chaincode;
import com.webank.weevent.core.fabric.sdk.FabricSDKWrapper;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class Test {

    public static void main(String[] args) {

         String topicName = "Topic";
         String topicName_1 = "abc";
         String channelName = "mychannel";
         Map<IConsumer.SubscribeExt, String> ext = new HashMap<>();
         MyConsumerListener defaultListener = new MyConsumerListener();
         long transactionTimeout = 30000;
         String lastEventId = "";
         List<Pair<String, String>> chaincodeList = new ArrayList<>();



        try {
            FabricConfig fabricConfig = new FabricConfig();
            fabricConfig.load("classpath:fabric/fabric.properties");
            FabricDelegate fabricDelegate = new FabricDelegate();
            fabricDelegate.initProxy(fabricConfig);

            FabricBroker4Producer iProducer = new FabricBroker4Producer(fabricDelegate);
            FabricBroker4Consumer iConsumer = new FabricBroker4Consumer(fabricDelegate);
            //开启Consumer
            iConsumer.startConsumer();

            Map<String, SubscriptionInfo> sublist = iConsumer.listSubscription(channelName);

            System.out.println(sublist.size());

            //查看是否某个topic是否存在
            //查看topic是否已经存在,若不存在就create它
//            boolean result = iProducer.open(topicName_1, channelName);
//            System.out.println(result);

//            TopicInfo i = iConsumer.state(topicName_1,channelName);

//            TopicInfo topicInfo = iProducer.state(topicName, channelName);
//
//            System.out.println(topicInfo);

            //check if chaincode is successful installed
//            chaincodeList = iConsumer.getContractInfo(channelName);
//            int len = chaincodeList.size();
//            for(int i =0; i< len ;i++) {
//                System.out.println(chaincodeList.get(i));
//            }


//            //查看是否已经成功create consumer和producer
//            System.out.println(iConsumer.startConsumer());
//            System.out.println(iProducer.startProducer());

            //查看是否可以在某个topic中发布content
            String data = String.format("i love you %s", System.currentTimeMillis());
            WeEvent weEvent = new WeEvent(topicName_1, data.getBytes());
            SendResult sendResultDto = iProducer.publish(weEvent, channelName).get(transactionTimeout, TimeUnit.MILLISECONDS);
            lastEventId = sendResultDto.getEventId();
            System.out.println(lastEventId);

//            System.out.println(sendResultDto.getStatus());
            //System.out.println(sendResult.get(transactionTimeout, TimeUnit.MILLISECONDS).getStatus().toString());


                //检测是否可以订阅某个topic
//            ext.put(IConsumer.SubscribeExt.InterfaceType, "junit");
//            ext.put(IConsumer.SubscribeExt.SubscriptionId, "ec1776da-1748-4c68-b0eb-ed3e92f9aadb");
//            ext.put(IConsumer.SubscribeExt.TopicTag, "Topic_Tag");
//            ext.put(IConsumer.SubscribeExt.RemoteIP, "localhost");
//            String subscriptionId = iConsumer.subscribe(topicName_1, channelName, WeEvent.OFFSET_LAST, ext, defaultListener);
//            iConsumer.unSubscribe("ec1776da-1748-4c68-b0eb-ed3e92f9aadb");
//
//            WeEvent event = iConsumer.getEvent(lastEventId, channelName);
//
//            System.out.println(event.getTopic());
//            Long blockHeight = iConsumer.getBlockHeight(channelName);
//            //test the list of topic
//            TopicPage topicPage = iProducer.list(0, 10, channelName);
//            List<WeEvent> EventList = iConsumer.loop(blockHeight - 9, channelName);
//            System.out.println(EventList.get(0).getContent()[1]);
            System.out.println(defaultListener.notifiedEvents.isEmpty());
        } catch (BrokerException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public static boolean push(String subscriptionId, String topicName, String eventContent){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.initialize();
        MyConsumerListener.MyListener listener = new MyConsumerListener.MyListener();
        NotifyTask notifyTask = new NotifyTask(subscriptionId, 1000, listener);
        threadPoolTaskExecutor.execute(notifyTask);

        List<WeEvent> data = new ArrayList<>();
        data.add(newEvent("a", topicName, eventContent));

        notifyTask.push(data);
        if(listener.received == 1) return true;

        return false;
    }

    public static WeEvent newEvent(String eventId, String topicName, String eventContent) {
        WeEvent event = new WeEvent(topicName, eventContent.getBytes(StandardCharsets.UTF_8));
        event.setEventId(eventId);
        return event;
    }

        public static class MyConsumerListener implements IConsumer.ConsumerListener {
        public List<String> notifiedEvents = new ArrayList<>();

        @Override
        public void onEvent(String subscriptionId, WeEvent event) {
            log.info("********** {}", event);

            this.notifiedEvents.add(event.getEventId());
        }

        @Override
        public void onException(Throwable e) {
            log.error("**********", e);

            this.notifiedEvents.clear();
        }



        static class MyListener implements IConsumer.ConsumerListener {
            public long received = 0;
            public String subscriptionId;

            @Override
            public void onEvent(String subscriptionId, WeEvent event) {
                this.subscriptionId = subscriptionId;
                received++;
            }

            @Override
            public void onException(Throwable e) {
                    received = -10000;
                }
        }
    }
}
