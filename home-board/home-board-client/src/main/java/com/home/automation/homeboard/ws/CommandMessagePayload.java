package com.home.automation.homeboard.ws;

public class CommandMessagePayload implements WSMessagePayload {

    private String commandId;
    private String userName;

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
