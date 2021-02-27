package com.home.automation.dispatcher.wsclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.util.Assert;

import java.lang.reflect.Type;

// TODO: abstractize
// TODO: use log4j
public class WsBoardStompSessionHandler extends StompSessionHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WsBoardStompSessionHandler.class);

    private WsBoardClient wsBoardClient;

    public void setWsBoardClient(final WsBoardClient wsBoardClient) {
        this.wsBoardClient = wsBoardClient;
    }

    @Override
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        checkWsBoardClient();
        logger.info("Client ws connected to " + wsBoardClient.getUrl());
        this.wsBoardClient.setConnected(true);
    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
        logger.info("Exception occured from ws server " + wsBoardClient.getUrl());
        this.wsBoardClient.setConnected(false);
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {
        logger.warn("transport error from ws server " + wsBoardClient.getUrl());
        wsBoardClient.setConnected(false);
    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        return null;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {
        System.out.println("frame handler");
        // TODO: add frame handler
    }

    private void checkWsBoardClient() {
        Assert.notNull(wsBoardClient, "WS board client must not be null!!");
    }
}
