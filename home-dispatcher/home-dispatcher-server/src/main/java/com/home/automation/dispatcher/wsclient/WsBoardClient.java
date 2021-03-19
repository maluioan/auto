package com.home.automation.dispatcher.wsclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;


public class WsBoardClient {

    private static final Logger logger = LoggerFactory.getLogger(WsBoardClient.class);
    private static final String SUBSCRIBER_IDENTIFIER = "subscriberID";

    private final WsBoardStompSessionHandler sessionHandler;
    private final WebSocketStompClient wsStompClient;
    private boolean connecting;
    private String url;
    private StompSession stompSession;
    private String boardCommunicationPath;
    private String clientId;

    public WsBoardClient() {
        wsStompClient = new WebSocketStompClient(new StandardWebSocketClient()) {
            @Override
            protected ConnectionHandlingStompSession createSession(@Nullable StompHeaders connectHeaders, StompSessionHandler handler) {
                DefaultStompSession session = new WsNonSubscripableStompSession(handler, super.processConnectHeaders(connectHeaders));
                session.setMessageConverter(super.getMessageConverter());
                session.setTaskScheduler(super.getTaskScheduler());
                session.setReceiptTimeLimit(super.getReceiptTimeLimit());
                return session;
            }
        };
        wsStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        sessionHandler = new WsBoardStompSessionHandler();
    }

    public boolean isConnected() {
        return connecting || (this.stompSession != null && stompSession.isConnected());
    }

    public void setConnecting(boolean connecting) {
        this.connecting = connecting;
    }

    public void connect() {
        try {
            setConnecting(true);
            wsStompClient.connect(getUrl(), createHandshakeHeaders(), getSessionHandler());
        } catch (Exception e) {
            setConnecting(false);
            logger.error("Http connection server error occured: " + e.getMessage());
        }
    }

    private WebSocketHttpHeaders createHandshakeHeaders() {
        final WebSocketHttpHeaders wsHeaders = new WebSocketHttpHeaders();
        wsHeaders.add("Authorization", "Basic TODO: add some key here");
        wsHeaders.add(SUBSCRIBER_IDENTIFIER, clientId);
        return wsHeaders;
    }

    private void setStompClientSession(StompSession stompSession) {
        this.stompSession = stompSession;
        setConnecting(false);
    }

    public void sendMessage(Object msg) {
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.send(boardCommunicationPath, msg);

        } else {
            logger.warn(String.format("Session %s disconnected", stompSession.getSessionId()));
        }
    }

    public void disconnect() {
        // TODO: test
        if (stompSession != null) {
            stompSession.disconnect();
        }
    }

    public WsBoardStompSessionHandler getSessionHandler() {
        return sessionHandler;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setBoardCommunicationPath(String boardCommunicationPath) {
        this.boardCommunicationPath = boardCommunicationPath;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    private class WsBoardStompSessionHandler extends StompSessionHandlerAdapter {

        @Override
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            logger.info("Client ws connected to " + WsBoardClient.this.getUrl());
            WsBoardClient.this.setStompClientSession(stompSession);
        }

        @Override
        public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
            logger.info("Exception occured from ws server " + WsBoardClient.this.getUrl());
            if (!stompSession.isConnected()) {
                WsBoardClient.this.stompSession = null;
            }
        }

        @Override
        public void handleTransportError(StompSession stompSession, Throwable throwable) {
            logger.warn("transport error from ws server " + WsBoardClient.this.getUrl());
            WsBoardClient.this.setConnecting(false);
            if (!stompSession.isConnected()) {
                WsBoardClient.this.stompSession = null;
            }
        }

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return null;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            System.out.println("frame handler: " + o);
            // TODO: add frame handler
        }
    }

}