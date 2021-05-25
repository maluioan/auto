package com.home.automation.homeboard.websocket.handler;

import com.home.automation.homeboard.websocket.message.MessageType;
import com.home.automation.homeboard.websocket.message.request.PioRequest;
import com.home.automation.homeboard.websocket.message.request.StompRequest;
import com.home.automation.homeboard.ws.SimpleWSMessagePayload;
import com.home.automation.homeboard.ws.WSMessagePayload;
import org.springframework.http.MediaType;

public class SimplePioRequestHandler extends AbstractWsRequestHandler<PioRequest> {

    @Override
    protected void handleInternal(final PioRequest request) {
        // TODO: convert pio => stomp
        getMessageMediator().handleDispatchMessage(createStompRequest(request));
    }

    @Override
    public boolean canHandle(PioRequest wsMessage) {
        return !MessageType.CONNECTED.equals(wsMessage.getMessageType());
    }

    private StompRequest createStompRequest(final PioRequest request) {
        final StompRequest req = new StompRequest();
        req.setMessageType(request.getMessageType());
        req.setMessageId(request.getMessageId().orElse(null));
        req.addHeader("bid", request.getBoardId());
        req.addHeader("actionId", request.getExecutorId());
        req.setPayload(createWsPayload(request));
        req.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return req;
    }

    private WSMessagePayload createWsPayload(final PioRequest request) {
        final SimpleWSMessagePayload simpleWSMessagePayload = new SimpleWSMessagePayload();
        simpleWSMessagePayload.setPayload(String.valueOf(request.getPayload()));
        return simpleWSMessagePayload;
    }
}
