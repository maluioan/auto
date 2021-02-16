package com.home.automation.homeboard.websocket.message;

public class BoardCommandMessage extends BaseBoardMessage {

    public enum MessageType {CONNECT}

    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
