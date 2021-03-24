package com.home.automation.homeboard.websocket.message.request;

// TODO: maybe create an inner class in the proper handler
public class CommandRequestMessage extends BoardRequestMessage
{
    private String commandId;
    private String userName;

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }
}
