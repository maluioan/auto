package com.home.automation.homeboard.ws;

public interface WSMessagePayload {
    public static String PAYLOAD_HEADER = "payload-type";

    default Class getType() {
        return this.getClass();
    }
}
