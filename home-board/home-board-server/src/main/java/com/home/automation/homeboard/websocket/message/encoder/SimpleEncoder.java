package com.home.automation.homeboard.websocket.message.encoder;

import com.home.automation.homeboard.websocket.endpointclient.AbstractWsEndpoint;
import com.home.automation.homeboard.websocket.message.converter.WSRequestConverter;
import com.home.automation.homeboard.websocket.message.request.WSRequest;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class SimpleEncoder implements Encoder.Text<WSRequest> {

    private WSRequestConverter<WSRequest> msgConverter;

    @Override
    public String encode(WSRequest msg) throws EncodeException {
        return msgConverter.encodeMessage(msg);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        msgConverter = (WSRequestConverter) endpointConfig.getUserProperties().get(AbstractWsEndpoint.ENCODER_CONVERTER);
    }

    @Override
    public void destroy() {

    }
}