package com.home.automation.homeboard.websocket.message.converter;

import com.home.automation.homeboard.websocket.message.BoardRequestMessage;
import com.home.automation.homeboard.websocket.message.ReceivedCommandMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StompServerConnectionMessageCoder implements BoardMessageCoder<BoardRequestMessage> {

    @Override
    public Optional<BoardRequestMessage> decodeMessage(String msg) {
        final String[] split = msg.split("\n");
        if (split.length < 1) {
            return Optional.empty();
        }
        final ReceivedCommandMessage.MessageType msgType = findMessageType(split[0]);
        return Optional.of(createInitialConnectionMessage(msgType, msg));
    }

    @Override
    public String encodeMessage(BoardRequestMessage msg) {
         // TODO: add recevied command logic here
        return msg.toString();
    }

    private ReceivedCommandMessage.MessageType findMessageType(final String command) {
        return ReceivedCommandMessage.MessageType.valueOf(command);
    }

    private BoardRequestMessage createInitialConnectionMessage(final ReceivedCommandMessage.MessageType command, final Object msg) {
        final ReceivedCommandMessage bcm = new ReceivedCommandMessage();

        final String[] stompParts = msg.toString().split("\n\n");
        if (stompParts.length < 2) {
            return bcm;
        }
        bcm.setCommand(command);
        bcm.setHeaders(createMapHeader(stompParts[0]));
        bcm.setOriginalPayload(stompParts[1]);
        return bcm;
    }

    private Map<String, String> createMapHeader(String commandHeaders) {
        final Map<String, String> headers = new HashMap<>();

        final String[] commHeadersParts = commandHeaders.split("\n");
        for (int i = 0; i < commHeadersParts.length; i++) {
            if (i > 0) {
                final String headerString = commHeadersParts[i];
                String[] split = headerString.split(":");
                headers.put(split[0], split[1]);
            }
        }
        return headers;
    }

}
