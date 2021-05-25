package com.home.automation.homeboard.ws;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface WSMessagePayload {
    String PAYLOAD_HEADER = "payload-type";

    @JsonIgnore
    default Class getType() {
        return this.getClass();
    }
}
