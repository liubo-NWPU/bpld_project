package com.gis.trans.config;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint("/websocket")
//@Component
public class WebSocketEnPoint {

    private static CopyOnWriteArraySet<WebSocketEnPoint> websocketSet = new CopyOnWriteArraySet<WebSocketEnPoint>();
    private Session session;

    private final static String GUIST_NAME = "游客";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private final String nickname;

    public WebSocketEnPoint() {
        nickname = GUIST_NAME + connectionIds.getAndIncrement();
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        websocketSet.add(this);
        String message = String.format("* %s%s", nickname, "已连接");
        try {
            sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose() throws IOException {
        String message = String.format("* %s%s", nickname, "已关闭连接");
        try {
            sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //   session.close();
        websocketSet.remove(this);
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("来自客户端的消息" + message);

        for (WebSocketEnPoint item : websocketSet) {
            try {
                item.sendMessage(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "******");
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }

    }

    @OnError
    public void onError(Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        if (session.isOpen()){
            this.session.getBasicRemote().sendText(message);
        }
    }

    public static void sendInf(String message) {
        for (WebSocketEnPoint item : websocketSet) {
            try {
                item.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
