package com.yjs.communicationsystem.weEvent.javaSDK;

import com.webank.weevent.client.IWeEventClient;
import com.webank.weevent.client.SendResult;

public class keyDistribution {
    public static void main(String[] args) throws Exception {
//        IWeEventClient client = IWeEventClient.builder().brokerUrl("http://47.242.5.78:7000/weevent-broker").groupId("mychannel").timeout(8000000).build();
        IWeEventClient client = IWeEventClient.builder().brokerUrl("http://localhost:7000/weevent-broker").userName("user").password("123456").groupId("mychannel").build();
        String hash = "e67d66080bc90690230758418f3094c2bd6e66019a4d041dcd17fa307daafdfc";
        String channelName = "mychannel";
        String chaincodeName = "IoT";
        String[] deviceAttribute = {"objectClass:inetOrgPerson", "objectClass:organizationalPerson",
                "sn:student2", "cn:student2", "uid:student2", "userPassword:student2", "ou:idp", "o:computer", "mail:student2@sdu.edu.cn", "title:student"};
        SendResult sendResult = JavaSDK.keyDistribution(client, hash, deviceAttribute, chaincodeName, channelName);
        System.out.println(sendResult);
        return;
    }
}