package com.home.automation.homeboard.websocket.endpointclient;

import com.home.automation.homeboard.websocket.Subscriber;
import com.home.automation.homeboard.websocket.handler.WsRequestHandler;
import com.home.automation.homeboard.websocket.mediator.MessageMediator;
import com.home.automation.homeboard.websocket.mediator.SubscriberRegistry;
import com.home.automation.homeboard.websocket.message.converter.WSRequestConverter;
import com.home.automation.homeboard.websocket.message.decoder.SimpleDecoder;
import com.home.automation.homeboard.websocket.message.encoder.SimpleEncoder;
import com.home.automation.homeboard.websocket.message.request.WSRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import javax.websocket.*;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public abstract class AbstractWsEndpoint<REQUEST extends WSRequest> extends Endpoint implements Subscriber {

    protected final Logger logger = LogManager.getLogger(AbstractWsEndpoint.class);

    public static final String ENCODER_CONVERTER = "encoderConverter";
    public static final String DECODER_CONVERTER = "decoderConverter";
    private static final String HANDSHAKE_REQUEST = "handshakeRequest";

    private final EndpointWsMessageHandler msgHandler = new EndpointWsMessageHandler();

    private SubscriberRegistry subscriberRegistry;
    private MessageMediator messageMediator;

    private WSRequestConverter<REQUEST> coderEncoderConverter;
    private List<WsRequestHandler<REQUEST>> requestMessageHandlers;
    protected HandshakeRequest handshakeRequest;
    protected Session wsSession;
    private String path;

    /**
     *
     * @param session
     * @param ec
     */
    @Override
    public void onOpen(Session session, EndpointConfig ec)
    {
        this.handshakeRequest = (HandshakeRequest)ec.getUserProperties().get(HANDSHAKE_REQUEST);
        this.wsSession = session;
        this.wsSession.addMessageHandler(msgHandler);
        onOpenInternal(session);
    }

    /**
     *
     * @param session
     */
    protected void onOpenInternal(Session session) {}

    /**
     *
     * @param boardMessage
     */
    protected abstract void onMessageInternal(REQUEST boardMessage);

    public void disconnect() {
        try {
            this.wsSession.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Disconnection from server"));
        } catch (IOException e) {
            throw new RuntimeException("Cannot close connection", e);
        }
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Subscriber identified by %s left, reason %s", getIdentifier(), closeReason.getReasonPhrase()));
        this.wsSession = null;
    }

    /**
     *
     * @param sec
     * @param request
     * @param response
     */
    public void handleHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response){
        // Hmmmm... not per user session context.. remove
        sec.getUserProperties().put(HANDSHAKE_REQUEST, request);
        sec.getUserProperties().put(ENCODER_CONVERTER, coderEncoderConverter);
        sec.getUserProperties().put(DECODER_CONVERTER, coderEncoderConverter);
    }

    /**
     *
     */
    @Override
    public void sendMessage(Object messageEndpoint, boolean asynch) {
        if (asynch) {
            wsSession.getAsyncRemote().sendObject(messageEndpoint);
        } else {
            try {
                // TODO: treat exceptions
                wsSession.getBasicRemote().sendObject(messageEndpoint);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param messageObject
     */
    @Override
    public void sendMessage(Object messageObject) {
        sendMessage(messageObject, false);
    }

    protected String getHeaderValue(final String headerName) {
        final List<String> bids = handshakeRequest.getHeaders().get(headerName);
        return (CollectionUtils.isNotEmpty(bids)) ? bids.get(0) : null;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setCoderEncoderConverter(WSRequestConverter coderEncoderConverter) {
        Assert.notNull(coderEncoderConverter, "Board msg converter should not be null");
        this.coderEncoderConverter = coderEncoderConverter;
    }

    protected List<WsRequestHandler<REQUEST>> getRequestMessageHandlers()
    {
        return requestMessageHandlers;
    }

    public void setRequestMessageHandlers(List<WsRequestHandler<REQUEST>> requestMessageHandlers)
    {
        this.requestMessageHandlers = requestMessageHandlers;
    }

    protected SubscriberRegistry getSubscriberRegistry() {
        return subscriberRegistry;
    }

    public void setSubscriberRegistry(SubscriberRegistry subscriberRegistry) {
        this.subscriberRegistry = subscriberRegistry;
    }

    protected MessageMediator getMessageMediator() {
        return messageMediator;
    }

    public void setMessageMediator(MessageMediator messageMediator) {
        this.messageMediator = messageMediator;
    }

    public List<Class<? extends Encoder>> getEncoders() {
        return Collections.singletonList(SimpleEncoder.class);
    }

    public List<Class<? extends Decoder>> getDecoders() {
        return Collections.singletonList(SimpleDecoder.class);
    }

    /**
     * message handler class
     */
    class EndpointWsMessageHandler implements MessageHandler.Whole<WSRequest> {

        @Override
        public void onMessage(final WSRequest stompRequest) {
            try {
                stompRequest.setInitiatingSubscriber(AbstractWsEndpoint.this);
                onMessageInternal((REQUEST) stompRequest);
            } catch (Exception e) {
                logger.error("Failed on message internal for msg %s, for type %s " + stompRequest.getMessageId());
                throw new RuntimeException("Error on receving message"); // TODO: add a custom exception
            }
        }
    }
}
