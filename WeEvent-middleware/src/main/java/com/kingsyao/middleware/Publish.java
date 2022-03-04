package com.kingsyao.middleware;

public class Publish {

    // 如何通过HTTP协议来实现这个发布服务，在HTTP header中传一个certificate hash来识别当前用户的身份
    // 1：group owner发布decrypted key给user（实现group owner在fabric上验证user的身份，并通过用户的证书解析属性+很大的随机数（确保不重复）来生成对应的decryption key）
    // 2：user利用public parameter以及access policy发布encrypted message给指定的user
}
