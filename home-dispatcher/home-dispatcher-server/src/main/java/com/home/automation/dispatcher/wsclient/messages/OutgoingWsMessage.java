package com.home.automation.dispatcher.wsclient.messages;

public class OutgoingWsMessage {
    private String commandId;
    private String user;
    private String createdDate;
    private String executionId;

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(final String commandId) {
        this.commandId = commandId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(final String user) {
        this.user = user;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final String createdDate) {
        this.createdDate = createdDate;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(final String executionId) {
        this.executionId = executionId;
    }
}
