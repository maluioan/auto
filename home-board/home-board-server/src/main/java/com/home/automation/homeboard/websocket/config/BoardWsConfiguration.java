package com.home.automation.homeboard.websocket.config;

import javax.websocket.server.ServerEndpointConfig;

public interface BoardWsConfiguration {

    String[] getPath();

    ServerEndpointConfig getServerEnpointConfig();
}
