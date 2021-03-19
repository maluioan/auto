package com.home.automation.dispatcher.client.impl;

import com.home.automation.dispatcher.client.DispatcherClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class DefaultDispatcherClient implements DispatcherClient {

    private final RestTemplate restTemplate;
    private String port;
    private String host;

    public DefaultDispatcherClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<String> getDispatcherToken(final String userName) {
        return restTemplate.getForEntity(String.format("http://%s:%s/dispatch/token?user={userName}", host, port), String.class, userName);
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
