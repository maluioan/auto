package com.home.automation.homeboard.websocket.handler;

import com.home.automation.homeboard.websocket.message.MessageType;
import com.home.automation.homeboard.websocket.message.request.PioRequest;

public class ConnectPioRequestHandler extends AbstractWsRequestHandler<PioRequest> {

    @Override
    protected void handleInternal(PioRequest request) {
        System.out.println("handle internal message from board!!!");
    }

    @Override
    public boolean canHandle(PioRequest wsMessage) {
        return MessageType.CONNECTED.equals(wsMessage.getMessageType());
    }
}
