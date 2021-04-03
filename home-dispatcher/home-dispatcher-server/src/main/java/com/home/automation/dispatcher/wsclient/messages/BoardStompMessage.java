package com.home.automation.dispatcher.wsclient.messages;

import com.home.automation.homeboard.ws.WSMessagePayload;
import org.springframework.messaging.simp.stomp.StompHeaders;

public class BoardStompMessage {
    private final StompHeaders headers = new StompHeaders();
    private WSMessagePayload payload;

    public StompHeaders getHeaders() {
        return headers;
    }

    public void addHeader(final String key, final String value) {
        this.headers.add(key, value);
    }

    public void addHeaders(final StompHeaders headers) {
        this.headers.addAll(headers);
    }

    public void setDestination(final String destination) {
        this.headers.setDestination(destination);
    }

    public void setMessageId(final String messageId) {
        this.headers.setMessageId(messageId);
    }

    public WSMessagePayload getPayload() {
        return payload;
    }

    public void setPayload(WSMessagePayload payload) {
        this.payload = payload;
    }
}
