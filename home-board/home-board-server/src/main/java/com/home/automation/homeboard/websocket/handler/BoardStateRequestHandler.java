package com.home.automation.homeboard.websocket.handler;

import com.home.automation.homeboard.websocket.message.MessageType;
import com.home.automation.homeboard.websocket.message.request.PioRequest;

public class BoardStateRequestHandler extends AbstractWsRequestHandler<PioRequest> {

    @Override
    public boolean canHandle(PioRequest wsMessage) {
        return MessageType.STATE.equals(wsMessage.getMessageType());
    }

    @Override
    protected void handleInternal(PioRequest request) {
        System.out.println(String.format("State of executor %s from board %s is %s", request.getExecutorId(), request.getBoardId(), request.getPayload()));
    }

}
