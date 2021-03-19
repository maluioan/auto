package com.home.automation.homeboard.websocket.message;

import org.apache.commons.collections4.MapUtils;

import java.util.Map;

@Deprecated
public class ReceivedCommandMessage extends BoardRequestMessage {

    public enum MessageType {CONNECT, SEND, CONNECTED, MESSAGE}

    private MessageType command;

    private Map<String, String> headers;

    private Long commandId;

    public MessageType getCommand() {
        return command;
    }

    public void setCommand(MessageType command) {
        this.command = command;
    }

    public Long getCommandId() {
        return commandId;
    }

    public void setCommandId(Long commandId) {
        this.commandId = commandId;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    // TODO: move this from here
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(command.name()).append("\n");
        MapUtils.emptyIfNull(headers).forEach((key, value) -> sb.append(key).append(":").append(value).append("\n"));
        sb.append("\n").append(getOriginalPayload());
        return sb.toString();
    }
}
