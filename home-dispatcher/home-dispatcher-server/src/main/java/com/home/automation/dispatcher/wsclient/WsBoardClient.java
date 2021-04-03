package com.home.automation.dispatcher.wsclient;

import com.home.automation.dispatcher.wsclient.frame.observers.FrameObserver;
import com.home.automation.dispatcher.wsclient.messages.BoardStompMessage;

public interface WsBoardClient {

    boolean isConnected();

    void connect();

    void disconnect();

    boolean sendMessage(BoardStompMessage msg);

    void addFrameObserver(FrameObserver frameObserver);
}