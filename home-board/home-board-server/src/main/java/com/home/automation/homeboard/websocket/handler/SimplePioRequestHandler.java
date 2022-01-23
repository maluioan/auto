package com.home.automation.homeboard.websocket.handler;

import com.home.automation.homeboard.websocket.message.MessageType;
import com.home.automation.homeboard.websocket.message.request.PioRequest;
import com.home.automation.homeboard.websocket.message.request.StompRequest;
import com.home.automation.homeboard.ws.DefaultMessagePayload;
import com.home.automation.homeboard.ws.WSMessagePayload;
import org.springframework.http.MediaType;

public class SimplePioRequestHandler extends AbstractWsRequestHandler<PioRequest> {

    @Override
    public boolean canHandle(final PioRequest wsMessage) {
        return !MessageType.CONNECTED.equals(wsMessage.getMessageType())
                && wsMessage.getMessageId().isPresent();
    }

    @Override
    protected void handleInternal(final PioRequest request) {
        getMessageMediator().handleDispatchMessage(createStompRequest(request));
    }

    private StompRequest createStompRequest(final PioRequest request) {
        final StompRequest req = new StompRequest();
        req.setMessageType(request.getMessageType());
//        req.addHeader("bid", request.getBoardId()); // TODO: avem nevoie de asta?
        req.setPayload(createWsPayload(request));
        req.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return req;
    }

    private WSMessagePayload createWsPayload(final PioRequest request) {
        final DefaultMessagePayload simpleWSMessagePayload = new DefaultMessagePayload();
        simpleWSMessagePayload.setPayload(String.valueOf(request.getPayload()));
        simpleWSMessagePayload.setExecutorId(request.getExecutorId());
        simpleWSMessagePayload.setMessageId(request.getMessageId().orElse(null));
        return simpleWSMessagePayload;
    }
}
