package com.home.automation.homeboard.websocket.mediator;

import com.home.automation.homeboard.websocket.message.request.PioRequest;
import com.home.automation.homeboard.websocket.message.request.StompRequest;

public interface MessageMediator {

    void handleDispatchMessage(StompRequest stompRequest);

    void handleBoardMessages(PioRequest pioRequest);
}
