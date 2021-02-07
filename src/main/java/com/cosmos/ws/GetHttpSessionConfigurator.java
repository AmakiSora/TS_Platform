package com.cosmos.ws;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession =(HttpSession) request.getHttpSession();
        //将httpsession对象存储进配置对象中
        sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}
