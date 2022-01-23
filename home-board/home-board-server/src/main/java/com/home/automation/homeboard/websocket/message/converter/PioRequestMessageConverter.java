package com.home.automation.homeboard.websocket.message.converter;

import com.home.automation.homeboard.websocket.message.MessageType;
import com.home.automation.homeboard.websocket.message.request.PioRequest;

import java.util.Optional;

public class PioRequestMessageConverter implements WSRequestConverter<PioRequest> {

    private static final String PIO_REQUEST_FORMAT = "%s;%s;%s;%s"; // boardId, executorId ,msgId, PAYLOAD

    // TODO: add checks
    @Override
    public Optional<PioRequest> decodeMessage(final String msg) {
        System.out.println("** Board msg ***   " + msg);
        final String[] mesgParts = msg.split(";");

        final PioRequest pio = new PioRequest();
        if (mesgParts.length == 1) {
            pio.setPayload("[NECUNOSCUT] [" + mesgParts[0] + "]");
            pio.setMessageType(MessageType.UNKNOWN);

        } else if ("Connected".equals(mesgParts[0])) {
            pio.setMessageType(MessageType.CONNECTED);
            pio.setBoardId(mesgParts[1]);
            pio.setPayload(msg);

        } else if ("State".equals(mesgParts[0])) {
            pio.setBoardId(mesgParts[1]);
            pio.setExecutorId(mesgParts[2]);
            pio.setPayload(mesgParts[3]);
            pio.setMessageType(MessageType.STATE);

        } else if ("stream".equals(mesgParts[0])) {
            populateStreamingPioRequest(mesgParts, pio);

        } else {
            pio.setBoardId(mesgParts[0]);
            pio.setExecutorId(mesgParts[1]);
            pio.setMessageId(mesgParts[2]);
            pio.setPayload(mesgParts[3]);
            pio.setMessageType(MessageType.MESSAGE);
        }
        return Optional.of(pio);
    }

    private void populateStreamingPioRequest(final String[] mesgParts, final PioRequest pio) {
        final String streamType = mesgParts[1];
        if (streamType.equals("video")) {
            pio.setMessageType(MessageType.VIDEO_STREAM);
        }

        pio.setBoardId(mesgParts[2]);
        pio.setExecutorId(mesgParts[3]);
        pio.setPayload(mesgParts[4]);
    }

    @Override
    public String encodeMessage(final PioRequest request) { // encode for board sending
        final String boardId = request.getBoardId();
        final String executorId = request.getExecutorId();
        final Optional<String> messageId = request.getMessageId();
        final Optional<Object> payload = Optional.ofNullable(request.getPayload());

        return String.format(PIO_REQUEST_FORMAT, boardId, executorId, messageId.orElse(""), payload.orElse(""));
    }
}
