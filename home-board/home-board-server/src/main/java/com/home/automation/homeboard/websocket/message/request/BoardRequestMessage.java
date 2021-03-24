package com.home.automation.homeboard.websocket.message.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.home.automation.homeboard.websocket.message.MessageType;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class BoardRequestMessage {

    @JsonIgnore
    protected StompRequest stompRequest;

    public void setStompRequest(StompRequest stompRequest) {
        this.stompRequest = stompRequest;
    }

    public String getExecutionId() {
        return stompRequest.getExecutionId().orElse(null);
    }

    public MessageType getCommand() {
        return stompRequest.getMessageType();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Object getMessageType()
    {
        return stompRequest.getMessageType();
    }
}
