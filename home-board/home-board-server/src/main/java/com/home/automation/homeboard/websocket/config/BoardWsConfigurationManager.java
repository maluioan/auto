package com.home.automation.homeboard.websocket.config;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Optional;

public class BoardWsConfigurationManager {

    private Collection<BoardWsConfiguration> wsConfigurations;

    public Collection<BoardWsConfiguration> getWsConfigurations() {
        return wsConfigurations;
    }

    public void setWsConfigurations(Collection<BoardWsConfiguration> wsConfigurations) {
        this.wsConfigurations = wsConfigurations;
    }

    public Optional<BoardWsConfiguration> findEndpointByPath(final String path) {
        return getWsConfigurations().stream()
                .filter(wsEndpoint -> checkPath(wsEndpoint.getPath(), path))
                .findFirst();
    }

    private boolean checkPath(String[] parts, String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }

        final String[] pathParts = path.split("/");
        if (parts.length > pathParts.length) {
            return false;
        }

        boolean validPath = true;
        for (int i = 0; i < parts.length; i++) {
            if (!StringUtils.equals(parts[i], pathParts[i])) {
                validPath = false;
                break;
            }
        }

         return validPath;
    }
}
