package com.home.automation.homeboard.websocket.mediator;

import com.home.automation.homeboard.websocket.message.DispatcherMessage;
import com.home.automation.homeboard.websocket.message.MicroControllerMessage;

public interface MessageMediator {

    // message handlers
    void handleMicroControllerMessage(MicroControllerMessage mcMessage);

    void handleMicroControllerMessage(DispatcherMessage commandMessage);
}
