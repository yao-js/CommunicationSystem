package com.kingsyao.middleware.websocket;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@ServerEndpoint(value = "/middleware/service/publish/{userId}")
@Component
@Slf4j
public class publishDemo implements Comparable<publishDemo>{

    private Session session;
    // 定义一个悲观锁
    static Lock lock = new ReentrantLock();

    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        log.info("ChatWebsocket open 有新连接加入 publish" + userId);
        log.info("ChatWebsocket open 有新连接加入 publish" + userId);
    }

    @OnClose
    public void onClose(){

    }

    @OnError
    public void onError(Throwable error){

    }

    @OnMessage
    public void onMessage(String message, Session session){

    }


    @Override
    public int compareTo(publishDemo o) {
        return 0;
    }
}
