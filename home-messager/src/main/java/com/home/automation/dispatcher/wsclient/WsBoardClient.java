package com.home.automation.dispatcher.wsclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.TimeUnit;

public class WsBoardClient {

    private static final Logger logger = LoggerFactory.getLogger(WsBoardClient.class);

    private StompSessionHandler sessionHandler;
    private WebSocketStompClient wsStompClient;
    private boolean connected;
    private String url;
    private long timeout;

    public WsBoardClient() {
        wsStompClient = new WebSocketStompClient(new StandardWebSocketClient());
        wsStompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void connect() {
        if (sessionHandler == null) {
            throw new IllegalStateException("ws client should have a messsage handler");
        }

        try {
            setConnected(true);
            final ListenableFuture<StompSession> connect = wsStompClient.connect(getUrl(), createHandshakeHandlers(), getSessionHandler());
            connect.addCallback(successCallbackMsg -> {
                System.out.println("successs callback: " + successCallbackMsg);
                WsBoardClient.this.setConnected(true);
            }, errorCallback -> {
                System.out.println("error callback: " + errorCallback);
                WsBoardClient.this.setConnected(false);
            });
//            StompSession stompSession = connect.get(timeout, TimeUnit.MILLISECONDS);
//            System.out.println(stompSession.getSessionId());
        } catch (Exception e) {
            logger.error("Http connection server error occured: " + e.getMessage());
        }
    }

    private WebSocketHttpHeaders createHandshakeHandlers() {
        final WebSocketHttpHeaders wsHeaders = new WebSocketHttpHeaders();
        wsHeaders.add("Authorization", "Basic TODO: add some key here");
        wsHeaders.add("subscriberId", "dispatcher");
        return wsHeaders;
    }

    public StompSessionHandler getSessionHandler() {
        return sessionHandler;
    }

    public void setSessionHandler(WsBoardStompSessionHandler sessionHandler) {
        sessionHandler.setWsBoardClient(this);
        this.sessionHandler = sessionHandler;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
