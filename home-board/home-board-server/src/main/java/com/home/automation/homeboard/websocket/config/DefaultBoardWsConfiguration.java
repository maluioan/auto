package com.home.automation.homeboard.websocket.config;

import com.home.automation.homeboard.websocket.endpointclient.AbstractWsEndpoint;

import javax.websocket.server.ServerEndpointConfig;

public class DefaultBoardWsConfiguration implements BoardWsConfiguration {

    private AbstractWsEndpoint abstractWsEndpoint;

    @Override
    public String[] getPath() {
        final String path = abstractWsEndpoint.getPath();
        return path != null ? path.split("/") : new String[]{};
    }

    public void setAbstractWsEndpoint(AbstractWsEndpoint abstractWsEndpoint) {
        this.abstractWsEndpoint = abstractWsEndpoint;
    }

    @Override
    public ServerEndpointConfig getServerEnpointConfig() {
        return new BoardEndpointRegistration(abstractWsEndpoint);
    }

}