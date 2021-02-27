package com.home.automation.homeboard.websocket.message.encoder;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class SimpleEncoder implements Encoder.Text<Object> {

    @Override
    public String encode(Object o) throws EncodeException {
        return o.toString();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}