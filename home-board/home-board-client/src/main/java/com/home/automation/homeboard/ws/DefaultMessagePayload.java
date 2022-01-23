package com.home.automation.homeboard.ws;

public class DefaultMessagePayload implements WSMessagePayload {

    private String messageId;
    private String executorId;
    private String actionId;
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

    @Override
    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }
}
