package com.home.automation.homeboard.websocket.handler;

import com.home.automation.homeboard.websocket.message.MessageType;
import com.home.automation.homeboard.websocket.message.request.PioRequest;

public class ConnectPioRequestHandler extends AbstractWsRequestHandler<PioRequest> {

    @Override
    protected void handleInternal(PioRequest request) {
        final PioRequest stateRequest = new PioRequest();
        stateRequest.setExecutorId("report");
        stateRequest.setInitiatingSubscriber(request.getInitiatingSubscriber());
        stateRequest.setBoardId(request.getBoardId());
        stateRequest.setMessageId("12"); // TODO: modifica esp32 code, pune sa suporte portiuni lipsa
        stateRequest.setPayload("oer");

        getMessageMediator().handleBoardMessages(stateRequest);
    }

    @Override
    public boolean canHandle(PioRequest wsMessage) {
        return MessageType.CONNECTED.equals(wsMessage.getMessageType());
    }
}
