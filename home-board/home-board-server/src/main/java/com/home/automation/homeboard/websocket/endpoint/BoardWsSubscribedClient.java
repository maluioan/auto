package com.home.automation.homeboard.websocket.endpoint;

import com.home.automation.homeboard.websocket.message.BaseBoardMessage;
import com.home.automation.homeboard.websocket.message.converter.AbstractBoardMessageConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import javax.websocket.*;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Optional;

public abstract class BoardWsSubscribedClient<MSG extends BaseBoardMessage> extends Endpoint {

    private final Logger logger = LogManager.getLogger(BoardWsSubscribedClient.class);

    private static final String HANDSHAKE_REQUEST = "handshakeRequest";
    protected HandshakeRequest handshakeRequest;
    protected Session wsSession;

    private BoardWsMessageHandler msgHandler = new BoardWsMessageHandler();
    private AbstractBoardMessageConverter<MSG> messageConverter;
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
     * @param sec
     * @param request
     * @param response
     */
    public void handleHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response){
        // Hmmmm... not per user session context..
        sec.getUserProperties().put(HANDSHAKE_REQUEST, request);
    }

    /**
     *
     */
    protected void sendMessage(Object messageEndpoint, boolean asynch) {
        if (asynch) {
            wsSession.getAsyncRemote().sendObject(messageEndpoint);
        } else {
            wsSession.getBasicRemote().sendObject(messageEndpoint);
        }
    }

    protected void sendMessage(Object messageEndpoint) {
        sendMessage(messageEndpoint);
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
    protected void onMessageInternal(MSG boardMessage) {}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public AbstractBoardMessageConverter getMessageConverter() {
        return messageConverter;
    }

    public void setMessageConverter(AbstractBoardMessageConverter messageConverter) {
        Assert.notNull(messageConverter, "Board msg converter should not be null");
        this.messageConverter = messageConverter;
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
