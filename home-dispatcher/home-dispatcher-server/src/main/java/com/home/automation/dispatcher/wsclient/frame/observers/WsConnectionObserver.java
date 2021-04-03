package com.home.automation.dispatcher.wsclient.frame.observers;

public interface WsConnectionObserver {
    void beforeConnection();
    void afterConnection();
    void beforeDisconnect();
    void afterDisconnect();
}
