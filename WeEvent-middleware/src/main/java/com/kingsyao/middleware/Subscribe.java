package com.kingsyao.middleware;

public class Subscribe {
    // 这里是不是需要用到websocket长链接呢？我觉得是需要的，因为客户端需要实时的通信以及消息的持续推送，因此我觉得传统的http协议无法满足要求
    // 当本地用户订阅了某个topic后，这个与服务器之间的连接需要长时间保持的吧
    // 而且还有要与IPFS接口进行调用
}
