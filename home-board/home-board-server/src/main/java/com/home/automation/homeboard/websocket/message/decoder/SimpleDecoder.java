package com.home.automation.homeboard.websocket.message.decoder;

import com.home.automation.homeboard.websocket.endpointclient.AbstractWsEndpoint;
import com.home.automation.homeboard.websocket.message.converter.WSRequestConverter;
import com.home.automation.homeboard.websocket.message.request.WSRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.NoSuchMessageException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.Optional;

public class SimpleDecoder implements Decoder.Text<WSRequest> {

    private static final Logger logger = LogManager.getLogger(SimpleDecoder.class);

    private WSRequestConverter<WSRequest> msgConverter;

    @Override
    // TODO: treat conversion exception
    public WSRequest decode(String message) throws DecodeException {
        final Optional<WSRequest> receivedCommandMessage = msgConverter.decodeMessage(message);
        if (!receivedCommandMessage.isPresent()) {
            logger.error(String.format("Invalid stomp message received %s", message));
            throw new NoSuchMessageException("cannot parse ws message");
        }
        return receivedCommandMessage.get();
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        msgConverter = (WSRequestConverter) endpointConfig.getUserProperties().get(AbstractWsEndpoint.DECODER_CONVERTER);
    }

    @Override
    public void destroy() {

    }
}
