package com.home.automation.homeboard.websocket.message;

public class DispatcherMessage extends BaseBoardMessage {

    public enum MessageType {CONNECT, COMMAND}

    private String command;

    private Long commandId;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Long getCommandId() {
        return commandId;
    }

    public void setCommandId(Long commandId) {
        this.commandId = commandId;
    }
}
