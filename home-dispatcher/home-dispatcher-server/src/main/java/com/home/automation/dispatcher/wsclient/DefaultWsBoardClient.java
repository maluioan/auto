package com.home.automation.dispatcher.wsclient;

import com.home.automation.dispatcher.wsclient.frame.observers.FrameObserver;
import com.home.automation.dispatcher.wsclient.messages.BoardStompMessage;
import com.home.automation.homeboard.ws.WSMessagePayload;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;


public class DefaultWsBoardClient implements WsBoardClient {

    private static final Logger logger = LoggerFactory.getLogger(DefaultWsBoardClient.class);
    private static final String SUBSCRIBER_IDENTIFIER = "subscriberID";

    private final WebSocketStompClient wsStompClient;
    private final StompSessionHandler sessionHandler;

    private FrameObserver frameObserver;
    private boolean connecting;
    private String url;
    private StompSession stompSession;
    private String boardCommunicationPath;
    private String clientId;

    public DefaultWsBoardClient() {
        wsStompClient = new WebSocketStompClient(new StandardWebSocketClient()) {
            @Override
            protected ConnectionHandlingStompSession createSession(StompHeaders connectHeaders, StompSessionHandler handler) {
                final DefaultStompSession session = new WsNonSubscripableStompSession(handler, super.processConnectHeaders(connectHeaders));
                session.setMessageConverter(super.getMessageConverter());
                session.setTaskScheduler(super.getTaskScheduler());
                session.setReceiptTimeLimit(super.getReceiptTimeLimit());
                return session;
            }
        };
        wsStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // TODO: refactor this
        final ThreadPoolTaskScheduler taskExecutor = new ThreadPoolTaskScheduler();
        taskExecutor.setPoolSize(1);
        taskExecutor.initialize();

        wsStompClient.setTaskScheduler(taskExecutor);
        sessionHandler = new WsBoardStompSessionHandler();
    }

    @Override
    public boolean isConnected() {
        return connecting || (this.stompSession != null && stompSession.isConnected());
    }

    @Override
    public void connect() {
        try {
            setConnecting(true); // TODO: check wsStompClient.isAutoStartup() && wsStompClient.isRunning();
            wsStompClient.connect(getUrl(), createHandshakeHeaders(), sessionHandler);
            wsStompClient.getTaskScheduler().scheduleAtFixedRate(() -> {
                if (connecting) {
                    setConnecting(false);
                }
            }, 15000);
        } catch (Exception e) {
            setConnecting(false);
            logger.error("Http connection server error occured: " + e.getMessage());
        }
    }

    @Override
    public boolean sendMessage(final BoardStompMessage msg) {
        if ((stompSession == null) || !stompSession.isConnected()) {
            logger.warn(String.format("Session %s disconnected", stompSession.getSessionId()));
            return false;
        }

        if (MapUtils.isNotEmpty(msg.getHeaders())) {
            final StompHeaders headers = msg.getHeaders();
            headers.setDestination(boardCommunicationPath);

            stompSession.send(headers, msg.getPayload());

        } else {
            stompSession.send(boardCommunicationPath, msg.getPayload());
        }
        return true;
    }

    @Override
    public void addFrameObserver(FrameObserver frameObserver) {
        this.frameObserver = frameObserver;
    }

    @Override
    public void disconnect() {
        // TODO: test
        if (stompSession != null) {
            stompSession.disconnect();
        }
    }

    private WebSocketHttpHeaders createHandshakeHeaders() {
        final WebSocketHttpHeaders wsHeaders = new WebSocketHttpHeaders();
        wsHeaders.add("Authorization", "Basic TODO: add some key here");
        wsHeaders.add(SUBSCRIBER_IDENTIFIER, clientId);
        return wsHeaders;
    }

    private void setStompClientSession(StompSession stompSession) {
        this.stompSession = stompSession;
        setConnecting(false);
    }

    protected void setConnecting(boolean connecting) {
        this.connecting = connecting;
    }

    protected String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setBoardCommunicationPath(String boardCommunicationPath) {
        this.boardCommunicationPath = boardCommunicationPath;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    // ws session handler
    class WsBoardStompSessionHandler extends StompSessionHandlerAdapter {

        private WsBoardStompSessionHandler() {}

        @Override
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            logger.info("Client ws connected to " + DefaultWsBoardClient.this.getUrl());
            DefaultWsBoardClient.this.setStompClientSession(stompSession);
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object framePayload) {
            final BoardStompMessage bsm = new BoardStompMessage();
            bsm.addHeaders(stompHeaders);
            bsm.setPayload((WSMessagePayload) framePayload);
            DefaultWsBoardClient.this.frameObserver.handleFrame(bsm);
        }

        @Override
        public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
            logger.info("Exception occured from ws server " + DefaultWsBoardClient.this.getUrl());
            DefaultWsBoardClient.this.setConnecting(false);
            if (!stompSession.isConnected()) {
                DefaultWsBoardClient.this.stompSession = null;
            }
        }

        @Override
        public void handleTransportError(StompSession stompSession, Throwable throwable) {
            // TODO: cand se trimite un msg care creapa, intra aici, nu ar trebui sa se inchida conexiunea tot timpul
            logger.warn("transport error from ws server " + DefaultWsBoardClient.this.getUrl());
            DefaultWsBoardClient.this.setConnecting(false);
            if (!stompSession.isConnected()) {
                DefaultWsBoardClient.this.stompSession = null;
            }
        }

        @Override
        public Type getPayloadType(final StompHeaders stompHeaders) {
            Type type = WSMessagePayload.class;

            // TODO: remove class name from payloadHeader, it makes it dependent to custom impl
            if (stompHeaders.containsKey(WSMessagePayload.PAYLOAD_HEADER)) {
                String payloadType = stompHeaders.get(WSMessagePayload.PAYLOAD_HEADER).get(0);
                try {
                    type = Class.forName(payloadType);
                } catch (ClassNotFoundException e) {
                    logger.error(String.format("Error creating payload type of %s", payloadType));
                }
            }

            return type;
        }
    }
}