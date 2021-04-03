package com.home.automation.dispatcher.wsclient;

import org.apache.commons.logging.Log;
import org.springframework.core.ResolvableType;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.simp.SimpLogging;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.Assert;

import java.lang.reflect.Type;
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
            Type payloadType = getSessionHandler().getPayloadType(stompNativeHeaders);
            Class<?> resolvedType = ResolvableType.forType(payloadType).resolve();
            if (resolvedType != null) {
                stompNativeHeaders.setSession(getSessionId());
                final Object messageObject = getMessageConverter().fromMessage(message, resolvedType);
                // TODO: treat null cases for messageObject
                getSessionHandler().handleFrame(stompNativeHeaders, messageObject);
            } else {
                throw new MessageConversionException("Unresolvable payload type [" + payloadType + "] from handler type [" + getSessionHandler().getClass() + "]");
            }
        } else {
            super.handleMessage(message);
        }
    }
}
