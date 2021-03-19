package com.home.automation.homeboard.websocket.message.converter;

import com.home.automation.homeboard.websocket.message.BoardRequestMessage;
import com.home.automation.homeboard.websocket.message.MicroControllerMessage;

import java.util.Optional;

@Deprecated // ???
public class MicroControllerMessageCoder implements BoardMessageCoder<BoardRequestMessage> {

    @Override
    public Optional<BoardRequestMessage> decodeMessage(final String msg) {
        MicroControllerMessage microControllerMessage = null;

        final String[] microcontrollerMessageSplit = msg.toString().split("\\|");
        if (microcontrollerMessageSplit.length > 3) {
            microControllerMessage = new MicroControllerMessage();
            microControllerMessage.setType("MESSAGE");

            microControllerMessage.setExecutionId(microcontrollerMessageSplit[0]);
            microControllerMessage.setBoardId(microcontrollerMessageSplit[1]);

            microControllerMessage.setOriginalPayload(microcontrollerMessageSplit[2]);

        }
        return Optional.ofNullable(microControllerMessage);
    }

    @Override
    public String encodeMessage(BoardRequestMessage msg) {
        return null;
    }
}
