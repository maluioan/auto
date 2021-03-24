package com.home.automation.homeboard.websocket.mediator;

import com.home.automation.homeboard.websocket.message.request.BoardRequestMessage;

public interface MessageMediator {

    // message handlers
    void handleDispatchMessage(BoardRequestMessage mcMessage);

}
