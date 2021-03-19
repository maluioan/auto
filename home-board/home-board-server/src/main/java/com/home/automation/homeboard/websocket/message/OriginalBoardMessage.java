package com.home.automation.homeboard.websocket.message;

import java.util.Map;

public class OriginalBoardMessage {
    private Object originalPayload;
    private Map<String, Object> messageHeaders;

    public Object getOriginalPayload() {
        return originalPayload;
    }

    public void setOriginalPayload(Object originalPayload) {
        this.originalPayload = originalPayload;
    }

    public Map<String, Object> getMessageHeaders() {
        return messageHeaders;
    }

    public void setMessageHeaders(Map<String, Object> messageHeaders) {
        this.messageHeaders = messageHeaders;
    }
}
