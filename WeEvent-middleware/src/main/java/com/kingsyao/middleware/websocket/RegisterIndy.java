package com.kingsyao.middleware.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@ServerEndpoint(value = "/middleware/service/registerIndy/{}")
@Component
@Slf4j
public class RegisterIndy implements Comparable<RegisterIndy> {
    private Session session;
    // 定义一个悲观锁
    static Lock lock = new ReentrantLock();

    @OnOpen
    public void onOpen(@PathParam("communicationType") String communicationType, Session session) {
        log.info("ChatWebsocket open 有新连接加入 userId: {}", communicationType);
        log.info("ChatWebsocket open 有新连接加入 userId: {}", communicationType);
    }

    @OnClose
    public void onClose(){
        log.info("");
    }

    @OnError
    public void onError(Throwable error){

    }

    @OnMessage
    public void onMessage(String message, Session session){

    }

    @Override
    public int compareTo(RegisterIndy o) {
        return 0;
    }
}
