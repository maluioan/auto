package com.home.automation.homeboard.ws;

public class SimpleWSMessagePayload implements WSMessagePayload {
    private String payload;

    public SimpleWSMessagePayload() {}

    public SimpleWSMessagePayload(String payload) {
        this.payload = payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }
}
