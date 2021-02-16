package com.home.automation.homeboard.websocket.message;

public class BaseBoardMessage {

    private Object payload;
    private String executionId;

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }
}
