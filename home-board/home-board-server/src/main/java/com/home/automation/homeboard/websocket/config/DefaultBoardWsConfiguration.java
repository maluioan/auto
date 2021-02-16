package com.home.automation.homeboard.websocket.config;

import com.home.automation.homeboard.websocket.endpoint.BoardWsSubscribedClient;

import javax.websocket.server.ServerEndpointConfig;

public class DefaultBoardWsConfiguration implements BoardWsConfiguration {

    private BoardWsSubscribedClient boardWsSubscribedClient;

    @Override
    public String[] getPath() {
        final String path = boardWsSubscribedClient.getPath();
        return path != null ? path.split("/") : new String[]{};
    }

    public void setBoardWsSubscribedClient(BoardWsSubscribedClient boardWsSubscribedClient) {
        this.boardWsSubscribedClient = boardWsSubscribedClient;
    }

    @Override
    public ServerEndpointConfig getServerEnpointConfig() {
        return new BoardEndpointRegistration(boardWsSubscribedClient);
    }

}