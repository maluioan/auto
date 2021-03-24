package com.home.automation.homeboard.websocket.config;

import com.home.automation.homeboard.websocket.endpointclient.AbstractWsEndpoint;
import org.springframework.util.Assert;

import javax.websocket.*;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.*;

class BoardEndpointRegistration extends ServerEndpointConfig.Configurator implements ServerEndpointConfig {

    private final AbstractWsEndpoint abstractWsEndpoint;

    BoardEndpointRegistration(final AbstractWsEndpoint abstractWsEndpoint) {
        Assert.notNull(abstractWsEndpoint, "Board ws endpoijtn must not be null");
        this.abstractWsEndpoint = abstractWsEndpoint;
    }

    @Override
    public Class<?> getEndpointClass() {
        return abstractWsEndpoint.getClass();
    }

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return (T) abstractWsEndpoint;
    }

    @Override
    public String getPath() {
        return abstractWsEndpoint.getPath();
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
        return abstractWsEndpoint.getEncoders();
    }

    @Override
    public List<Class<? extends Decoder>> getDecoders() {
        return abstractWsEndpoint.getDecoders();
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        abstractWsEndpoint.handleHandshake(sec, request, response);
    }

    @Override
    public Map<String, Object> getUserProperties() {
        return new HashMap<>(); // TODO: add origin check properties
    }
}
