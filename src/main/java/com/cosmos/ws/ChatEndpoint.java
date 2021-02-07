package com.cosmos.ws;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/student/chat.html",configurator = GetHttpSessionConfigurator.class)
@Component
public class ChatEndpoint {
    //用于存储每个客户端对象对应的chatEndpoint对象
    private static Map<String,ChatEndpoint> onlineUsers = new ConcurrentHashMap<>();

    //声明session对象，通过该对象可以发送消息给指定的用户
    private Session session;

    //声明一个HttpSession对象，用于显示存储的用户名
    private HttpSession httpSession;

    @OnOpen//连接建立时调用
    public void onOpen(Session session, EndpointConfig config){
        this.session = session;
        //获取httpSession对象
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.httpSession = httpSession;
        //从httpSession中获取用户名
        String name = (String)httpSession.getAttribute("name");
        //将当前对象存储到容器中
        onlineUsers.put(name,this);
        //将当前用户推送给所有人
        //1，获取消息
        String message = MessageUtils.getMessage(true,null,getNames());
        //2，调用方法进行系统消息推送
        broadcastAllUsers(message);

    }
    private void broadcastAllUsers(String message){//将信息推送给所有客户端
        try {
            Set<String> names = onlineUsers.keySet();
            for(String name:names){
                ChatEndpoint chatEndpoint = onlineUsers.get(name);
                chatEndpoint.session.getBasicRemote().sendText(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private Set<String> getNames(){//获取名单列表
        return onlineUsers.keySet();
    }
    @OnMessage//收到客户端发送数据时调用
    public void onMessage(String message,Session session){

    }
    @OnClose//连接断开时调用
    public void onClose(Session session){

    }
}
