package com.yjs.communicationsystem.ECDSA;

import java.util.Map;
import com.yjs.communicationsystem.cpabeApi.cpabe.*;
import com.yjs.communicationsystem.ECDH.*;

public class test {

    public static void main(String[] args) throws Exception {
//        Random random = new Random();
//        random.setSeed(1232314);
//        int n = random.nextInt();

        String pubParameterFile = "./cpabeKey/cpabePublicParameter/publicParameter";
        String masterKeyFile = "./cpabeKey/cpabePrivateKey/masterKey";
        String[] deviceAttribute = {"objectClass:inetOrgPerson", "objectClass:organizationalPerson",
                "sn:student2", "cn:student2", "uid:student2", "userPassword:student2", "ou:idp", "o:computer", "mail:student2@sdu.edu.cn", "title:student"};
        byte[] communication_key = Cpabe.keygen(pubParameterFile, masterKeyFile, deviceAttribute);

        long start_time = System.currentTimeMillis();
        Map<String, Object> Device_key = util.initKey();
        byte[] publicKey = util.getPublicKey(Device_key);
        byte[] so_tsk = util.getSecretKey(publicKey, util.getPrivateKey(Device_key));

//        Map<String, Object> so_key = util.initKey();
//        byte[] so_tsk = util.getSecretKey(publicKey, util.getPrivateKey(so_key));
//        byte[] enc = util.encrypt(communication_key, so_tsk);

//        byte[] d_tsk = util.getSecretKey(util.getPublicKey(so_key), util.getPrivateKey(Device_key));

//        byte[] dec = util.decrypt(enc, so_tsk);

        long end_time = System.currentTimeMillis();
        System.out.println(end_time-start_time);
    }

}
