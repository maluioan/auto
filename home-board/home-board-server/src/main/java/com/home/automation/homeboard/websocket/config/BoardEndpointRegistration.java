package com.home.automation.homeboard.websocket.config;

import com.home.automation.homeboard.websocket.endpointclient.AbstractWsClient;
import com.home.automation.homeboard.websocket.message.decoder.SimpleDecoder;
import com.home.automation.homeboard.websocket.message.encoder.SimpleEncoder;
import org.springframework.util.Assert;

import javax.websocket.*;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.*;

class BoardEndpointRegistration extends ServerEndpointConfig.Configurator implements ServerEndpointConfig {

    private final AbstractWsClient abstractWsClient;

    BoardEndpointRegistration(final AbstractWsClient abstractWsClient) {
        Assert.notNull(abstractWsClient, "Board ws endpoijtn must not be null");
        this.abstractWsClient = abstractWsClient;
    }

    @Override
    public Class<?> getEndpointClass() {
        return abstractWsClient.getClass();
    }

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return (T) abstractWsClient;
    }

    @Override
    public String getPath() {
        return abstractWsClient.getPath();
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
        // TODO: no encoder issue, nu se paote ocoli??
        return abstractWsClient.getEncoders();
    }

    @Override
    public List<Class<? extends Decoder>> getDecoders() {
        return abstractWsClient.getDecoders();
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        abstractWsClient.handleHandshake(sec, request, response);
    }

    @Override
    public Map<String, Object> getUserProperties() {
        return new HashMap<>(); // TODO: add origin check properties
    }
}
