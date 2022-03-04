//package com.yjs.communicationsystem.KeyManagement;
//
//import lombok.SneakyThrows;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class BatchKeyUpdateJob implements Job {
//    static List<String> deviceHashArray = new ArrayList();
//    static int keyUpdateRounds = 1;
//    @SneakyThrows
//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
////        String deviceHashListFileName = "DeviceHashList/input.txt";
////        //从本地的设备hash列表中读出所有需要更新abe key的设备hash
////        readFile(deviceHashListFileName);
////        String channelName = "mychannel";
////        String chaincodeName = "IoT";
////        //某批次设备的公有属性
////        String[] deviceAttribute = {"objectClass:inetOrgPerson", "objectClass:organizationalPerson",
////                "sn:student2", "cn:student2", "uid:student2", "userPassword:student2", "ou:idp", "o:computer", "mail:student2@sdu.edu.cn", "title:student"};
////        IPFS ipfs = new IPFS("/ip4/47.242.80.1/tcp/5001");
////        IWeEventClient client = IWeEventClient.builder().brokerUrl("http://localhost:7000/weevent-broker").userName("user").password("123456").groupId("mychannel").build();
////
////        for (int i=0; i<deviceHashArray.size(); i++){
////
////            deviceAbeKeyUpdate(deviceHashArray.get(i), channelName, chaincodeName, ipfs, client, deviceAttribute, keyUpdateRounds);
////
////        }
////        //记录每次更新abe key的轮数，每次scheduler执行后自动添加1
//        keyUpdateRounds += 1;
//
//        System.out.println(keyUpdateRounds);
//
//    }
//
//    public static void readFile(String fileName){
//        try (FileReader reader = new FileReader(fileName);
//             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
//        ) {
//            String line;
//            //网友推荐更加简洁的写法
//            while ((line = br.readLine()) != null) {
//                // 一次读入一行数据
//                String deviceHashData =line;
//                deviceHashArray.add(deviceHashData);
////                System.out.println(line);
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
