package com.home.automation.homeboard.websocket.handler;

import com.home.automation.homeboard.websocket.message.MessageType;
import com.home.automation.homeboard.websocket.message.request.PioRequest;

public class VideoStreamRequestHandler extends AbstractWsRequestHandler<PioRequest> {

    @Override
    public boolean canHandle(PioRequest wsMessage) {
        return MessageType.VIDEO_STREAM.equals(wsMessage.getMessageType());
    }

    @Override
    protected void handleInternal(final PioRequest request) {
        System.out.println("*** Received image: " + request.getPayload());
    }
}
