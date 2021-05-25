package com.home.automation.dispatcher.wsclient;

import com.home.automation.dispatcher.wsclient.frame.handlers.BoardFrameHandler;
import com.home.automation.dispatcher.wsclient.messages.BoardStompMessage;

public interface WsBoardClient {

    boolean isConnected();

    void connect();

    void disconnect();

    boolean sendMessage(BoardStompMessage msg);

    void setBoardFrameHandler(BoardFrameHandler boardFrameHandler);
}
