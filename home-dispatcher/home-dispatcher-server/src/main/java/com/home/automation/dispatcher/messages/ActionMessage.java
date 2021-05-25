package com.home.automation.dispatcher.messages;

public class ActionMessage {
    private String actionId; // TODO: asta pe FE nu are nici o valoare, scoate-l de pe toate data si nu-l pune in FE
    private String executorId;
    private Object payload;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }
}
