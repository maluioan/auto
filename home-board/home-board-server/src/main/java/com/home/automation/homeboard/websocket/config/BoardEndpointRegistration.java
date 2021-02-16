package com.home.automation.homeboard.websocket.config;

import com.home.automation.homeboard.websocket.endpoint.BoardWsSubscribedClient;
import org.springframework.util.Assert;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Extension;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.*;

class BoardEndpointRegistration extends ServerEndpointConfig.Configurator implements ServerEndpointConfig {

    private final BoardWsSubscribedClient boardWsSubscribedClient;

    BoardEndpointRegistration(final BoardWsSubscribedClient boardWsSubscribedClient) {
        Assert.notNull(boardWsSubscribedClient, "Board ws endpoijtn must not be null");
        this.boardWsSubscribedClient = boardWsSubscribedClient;
    }

    @Override
    public Class<?> getEndpointClass() {
        return boardWsSubscribedClient.getClass();
    }

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return (T) boardWsSubscribedClient;
    }

    @Override
    public String getPath() {
        return boardWsSubscribedClient.getPath();
    }

    @Override
    public List<String> getSubprotocols() {
        return Collections.emptyList();
    }

    @Override
    public List<Extension> getExtensions() {
        return Collections.emptyList();
    }

    @Override
    public Configurator getConfigurator() {
        return this;
    }

    @Override
    public List<Class<? extends Encoder>> getEncoders() {
        return Collections.emptyList();
    }

    @Override
    public List<Class<? extends Decoder>> getDecoders() {
        return Collections.emptyList();
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        boardWsSubscribedClient.handleHandshake(sec, request, response);
    }

    @Override
    public Map<String, Object> getUserProperties() {
        return new HashMap<>(); // TODO: add origin check properties
    }
}
