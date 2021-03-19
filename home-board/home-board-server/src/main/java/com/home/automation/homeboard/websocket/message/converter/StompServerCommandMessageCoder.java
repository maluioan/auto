package com.home.automation.homeboard.websocket.message.converter;

import com.home.automation.homeboard.websocket.message.BoardRequestMessage;

import java.util.Optional;

@Deprecated
public class StompServerCommandMessageCoder implements BoardMessageCoder<BoardRequestMessage> {

    @Override
    public Optional<BoardRequestMessage> decodeMessage(String msg) {
        return Optional.empty();
    }

    @Override
    public String encodeMessage(BoardRequestMessage msg) {
        return null;
    }
}
