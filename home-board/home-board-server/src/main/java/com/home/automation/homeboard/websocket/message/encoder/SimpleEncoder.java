package com.home.automation.homeboard.websocket.message.encoder;

import com.home.automation.homeboard.websocket.endpointclient.AbstractWsClient;
import com.home.automation.homeboard.websocket.message.BoardRequestMessage;
import com.home.automation.homeboard.websocket.message.converter.BoardMessageCoder;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class SimpleEncoder implements Encoder.Text<BoardRequestMessage> {

    private BoardMessageCoder<BoardRequestMessage> msgConverter;

    @Override
    public String encode(BoardRequestMessage msg) throws EncodeException {
        return msgConverter.encodeMessage(msg);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        msgConverter = (BoardMessageCoder) endpointConfig.getUserProperties().get(AbstractWsClient.ENCODER_CONVERTER);
    }

    @Override
    public void destroy() {

    }
}