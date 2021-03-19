package com.home.automation.homeboard.websocket.message.converter;

import com.home.automation.homeboard.websocket.message.BoardRequestMessage;

import java.util.Optional;

public interface BoardMessageCoder<T extends BoardRequestMessage> {

    Optional<T> decodeMessage(String msg);

    String encodeMessage(T msg);

}
