package com.home.automation.homeboard.websocket.message.converter;

import com.home.automation.homeboard.websocket.message.BaseBoardMessage;
import com.home.automation.homeboard.websocket.message.BoardCommandMessage;

import java.util.Optional;

public class StompServerConnectionMessageConverter extends AbstractBoardMessageConverter {

    @Override
    public Optional<BaseBoardMessage> handleMessage(final String msg) {
        final String[] split = msg.split("\n");
        if (split.length <= 3) {
            return next(msg);
        }
        return Optional.of(createInitialConnectionMessage(split[0], msg));
    }

    private BaseBoardMessage createInitialConnectionMessage(final String command, final String msg) {
        final BoardCommandMessage bcm = new BoardCommandMessage();
        bcm.setPayload(msg);
        bcm.setCommand(command);
        return bcm;
    }
}
