package com.home.automation.homeboard.websocket.endpointclient;

import com.home.automation.homeboard.websocket.Subscriber;
import com.home.automation.homeboard.websocket.mediator.MessageMediator;
import com.home.automation.homeboard.websocket.mediator.SubscriberRegistry;
import com.home.automation.homeboard.websocket.message.BaseBoardMessage;
import com.home.automation.homeboard.websocket.message.converter.AbstractBoardMessageConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import javax.websocket.*;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public abstract class BoardWsSubscribedClient<MSG extends BaseBoardMessage> extends Endpoint implements Subscriber {

    protected final Logger logger = LogManager.getLogger(BoardWsSubscribedClient.class);

    private static final String HANDSHAKE_REQUEST = "handshakeRequest";
    private final BoardWsMessageHandler msgHandler = new BoardWsMessageHandler();

    private SubscriberRegistry subscriberRegistry;
    private MessageMediator messageMediator;

    private AbstractBoardMessageConverter<MSG> messageConverter;
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
        session.addMessageHandler(msgHandler);
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
    protected abstract void onMessageInternal(MSG boardMessage);

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
     * @param messageEndpoint
     */
    @Override
    public void sendMessage(Object messageEndpoint) {
        sendMessage(messageEndpoint, false);
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

    public void setMessageConverter(AbstractBoardMessageConverter messageConverter) {
        Assert.notNull(messageConverter, "Board msg converter should not be null");
        this.messageConverter = messageConverter;
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

    /**
     * message handler class
     */
    class BoardWsMessageHandler implements MessageHandler.Whole<String> {

        @Override
        public void onMessage(String msg) {
            final Optional<MSG> baseBoardMessage = messageConverter.handleMessage(msg);
            if (baseBoardMessage.isPresent()) {
                onMessageInternal(baseBoardMessage.get());

            } else {
                logger.warn("No converters registered for msg " + msg);
            }
        }
    }
}
