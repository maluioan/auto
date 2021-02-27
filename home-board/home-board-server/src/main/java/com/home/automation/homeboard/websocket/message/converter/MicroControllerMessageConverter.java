package com.home.automation.homeboard.websocket.message.converter;

import com.home.automation.homeboard.websocket.message.BaseBoardMessage;
import com.home.automation.homeboard.websocket.message.MicroControllerMessage;

import java.util.Optional;

public class MicroControllerMessageConverter extends AbstractBoardMessageConverter {

    @Override
    public Optional<BaseBoardMessage> handleMessage(String msg) {
        final MicroControllerMessage microControllerMessage = new MicroControllerMessage();
        microControllerMessage.setPayload(msg);
        return Optional.of(microControllerMessage);
    }
}
