package com.home.automation.dispatcher.wsclient;

import org.springframework.scheduling.annotation.Scheduled;

public class WsClientConnectionScheduler {

    private WsBoardClient wsBoardClient;

    public WsClientConnectionScheduler(final WsBoardClient wsBoardClient) {
        this.wsBoardClient = wsBoardClient;
    }

    @Scheduled(fixedDelay = 5000)
    public void connectClient() {
        if (!wsBoardClient.isConnected()) {
            wsBoardClient.connect();
        }
    }
}
