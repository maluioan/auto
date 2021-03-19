package com.home.automation.homeboard.websocket.config;

import com.home.automation.homeboard.websocket.endpointclient.AbstractWsClient;

import javax.websocket.server.ServerEndpointConfig;

public class DefaultBoardWsConfiguration implements BoardWsConfiguration {

    private AbstractWsClient abstractWsClient;

    @Override
    public String[] getPath() {
        final String path = abstractWsClient.getPath();
        return path != null ? path.split("/") : new String[]{};
    }

    public void setAbstractWsClient(AbstractWsClient abstractWsClient) {
        this.abstractWsClient = abstractWsClient;
    }

    @Override
    public ServerEndpointConfig getServerEnpointConfig() {
        return new BoardEndpointRegistration(abstractWsClient);
    }

}