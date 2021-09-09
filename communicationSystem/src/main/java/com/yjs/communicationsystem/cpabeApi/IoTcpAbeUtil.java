package com.yjs.communicationsystem.cpabeApi;
import com.yjs.communicationsystem.cpabeApi.cpabe.Cpabe;

public class IoTcpAbeUtil {

//    public static void

    public static void main(String[] args) throws Exception {


        String pubParameterFile = "cpabeKey/cpabePublicParameter/publicParameter";
        String masterKeyFile = "cpabeKey/cpabePrivateKey/masterKey";
        String deviceCpAbeKey = "Device/deviceAbeKey/abeKey01";
        String[] student_attr = {"objectClass:inetOrgPerson objectClass:organizationalPerson",
                "sn:student2 cn:student2 uid:student2 userPassword:student2",
                "ou:idp o:computer mail:student2@sdu.edu.cn title:student"};
        String student_policy = "sn:student2 cn:student2 uid:student2 3of3";
        //CPABE setup
        Cpabe.setup(pubParameterFile, masterKeyFile);

        //CPABE key generation
//        Cpabe.keygen(pubParameterFile,masterKeyFile,deviceCpAbeKey, student_attr);

        //CPABE enc function
//        Cpabe.enc(pubParameterFile, student_policy, );

    }
}
