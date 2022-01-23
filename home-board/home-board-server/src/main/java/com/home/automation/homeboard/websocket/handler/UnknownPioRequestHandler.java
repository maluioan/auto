package com.home.automation.homeboard.websocket.handler;

import com.home.automation.homeboard.websocket.Subscriber;
import com.home.automation.homeboard.websocket.message.request.PioRequest;

public class UnknownPioRequestHandler extends AbstractWsRequestHandler<PioRequest> {

    @Override
    protected void handleInternal(final PioRequest request) {
        final Subscriber initiatingSubscriber = request.getInitiatingSubscriber();
        if (initiatingSubscriber != null) {
            System.out.println("Received unknown input from board: [" + initiatingSubscriber.getIdentifier() + "] with payload: '" + request.getPayload() + "'");
        } else {
            System.out.println("Received a total fcking mystery of a pio request");
        }
    }

    @Override
    public boolean canHandle(PioRequest request) {
        return !request.getMessageId().isPresent();
    }
}
