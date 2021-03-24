package com.home.automation.homeboard.websocket.message.request;

import com.home.automation.homeboard.websocket.message.MessageType;

import java.util.Map;
import java.util.Optional;


public class StompRequest extends AbstractWSRequest {

    private MessageType messageType;
    private Map<String, Object> messageHeaders;

    @Override
    public Optional<String> getExecutionId()
    {
        return getHeaderValue("execution-id");
    }

    public Map<String, Object> getMessageHeaders() {
        return messageHeaders;
    }

    public void setMessageHeaders(Map<String, Object> messageHeaders) {
        this.messageHeaders = messageHeaders;
    }

    public MessageType getMessageType()
    {
        return messageType;
    }

    public void setMessageType(MessageType messageType)
    {
        this.messageType = messageType;
    }

    public Optional<String> getRequestType() {
        return getHeaderValue("request-type");
    }

    private <T> Optional<T> getHeaderValue(final String key) {
        return Optional.ofNullable ((T)messageHeaders.get(key));
    }

    public Optional<String> getDestination()
    {
        return this.getHeaderValue("destination");
    }
}
