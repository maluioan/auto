package com.home.automation.homeboard.websocket.mediator;

import com.home.automation.homeboard.websocket.message.request.PioRequest;
import com.home.automation.homeboard.websocket.message.request.StompRequest;
import com.home.automation.homeboard.websocket.message.request.WSRequest;
import com.home.automation.homeboard.ws.WSMessagePayload;

public interface MessageMediator {

    void handleDispatchMessage(StompRequest stompRequest);

    void handleBoardMessages(PioRequest pioRequest);

    <T extends WSMessagePayload> void sendMessageBackToInitiatingSubscriber(WSRequest<T> request);
}
