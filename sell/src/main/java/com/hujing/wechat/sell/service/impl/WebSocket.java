package com.hujing.wechat.sell.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author hj
 * @time 2019-03-02 16:36
 * @description 操作webSocket业务类
 */
@Slf4j
@Component
@ServerEndpoint("/webSocket")
public class WebSocket {

    /*webSocket会话*/
    private Session session;

    /*储存webSocket连接的容器*/
    private static final CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 开启事件
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        log.info("【建立了一个新连接】 当前连接数：{}",webSocketSet.size());
    }

    /**
     * 关闭事件
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        log.info("【关闭连接】 当前连接数：{}",webSocketSet.size());
    }

    /**
     * 接受消息
     * @param msg
     */
    @OnMessage
    public void onMessage(String msg) {
        log.info("【收到消息】 收到客户端的消息={}",msg);
    }


    public void sendMsg(String msg){
        for (WebSocket webSocket : webSocketSet) {
            log.info("【webSocket开始广播消息】 msg={}",msg);
            try {
                webSocket.session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                log.error("【webSocket发送消息失败】");
            }
        }
    }


}
