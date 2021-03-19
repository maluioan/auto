package com.home.automation.dispatcher.wsclient;

import org.apache.commons.logging.Log;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpLogging;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

class WsNonSubscripableStompSession extends DefaultStompSession {

    private static final Log logger = SimpLogging.forLogName(WsNonSubscripableStompSession.class);

    public WsNonSubscripableStompSession(StompSessionHandler sessionHandler, StompHeaders connectHeaders) {
        super(sessionHandler, connectHeaders);
    }

    @Override
    public void handleMessage(Message<byte[]> message) {
        StompHeaderAccessor accessor = (StompHeaderAccessor) MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        Assert.state(accessor != null, "No StompHeaderAccessor");
        accessor.setSessionId(this.getSessionId());

        final StompCommand command = accessor.getCommand();
        final Map<String, List<String>> nativeHeaders = (Map<String, List<String>>)accessor.getHeader("nativeHeaders");
        final StompHeaders stompNativeHeaders = StompHeaders.readOnlyStompHeaders(nativeHeaders);

        if (StompCommand.MESSAGE.equals(command)) {
            final Object messageObject = getMessageConverter().fromMessage(message, String.class);
            getSessionHandler().handleFrame(stompNativeHeaders, messageObject);
        } else {
            super.handleMessage(message);
        }
    }
}
