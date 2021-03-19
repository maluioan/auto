package com.home.automation.homeboard.websocket.mediator;

import com.home.automation.homeboard.websocket.message.BoardRequestMessage;

public interface MessageMediator {

    // message handlers
    void handleDispatchMessage(BoardRequestMessage mcMessage);

//    void handleDispatchMessage(BoardRequestMessage boardMessage);
}
