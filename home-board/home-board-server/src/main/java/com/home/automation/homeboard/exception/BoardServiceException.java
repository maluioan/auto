package com.home.automation.homeboard.exception;

import com.home.automation.homeboard.ws.WSMessagePayload;

public class BoardServiceException extends RuntimeException {

    private WSMessagePayload payload;

    public BoardServiceException(final String message, Throwable throwable) {
        super(message, throwable);
    }

    public WSMessagePayload getPayload() {
        return payload;
    }

    public void setPayload(WSMessagePayload payload) {
        this.payload = payload;
    }
}
