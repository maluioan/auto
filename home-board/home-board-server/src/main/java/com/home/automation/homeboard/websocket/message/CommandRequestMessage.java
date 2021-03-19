package com.home.automation.homeboard.websocket.message;

public class CommandRequestMessage extends BoardRequestMessage {

    private String commandId;
    private String userName;

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }
}
