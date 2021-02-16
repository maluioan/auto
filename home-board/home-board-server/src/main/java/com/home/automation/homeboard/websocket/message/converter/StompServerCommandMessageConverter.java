package com.home.automation.homeboard.websocket.message.converter;

import com.home.automation.homeboard.websocket.message.BaseBoardMessage;

import java.util.Optional;

public class StompServerCommandMessageConverter extends AbstractBoardMessageConverter {

    @Override
    public Optional<BaseBoardMessage> handleMessage(String msg) {
        return Optional.empty();
    }
}
