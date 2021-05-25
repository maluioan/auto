package com.home.automation.homeboard.ws;

public class ActionMessagePayload implements WSMessagePayload {

    private String messageId;
    private String executorId;
    private Object payload; // TODO: check json serializer/deserializer

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String commandId) {
        this.messageId = commandId;
    }

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String userName) {
        this.executorId = userName;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
