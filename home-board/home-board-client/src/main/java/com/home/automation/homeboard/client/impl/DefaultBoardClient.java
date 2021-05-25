package com.home.automation.homeboard.client.impl;

import com.home.automation.homeboard.client.BoardClient;
import com.home.automation.homeboard.data.CommandDataList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class DefaultBoardClient implements BoardClient {

    private final RestTemplate restTemplate;
    private String port;
    private String host;

    public DefaultBoardClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<CommandDataList> findActiveCommandsByCount(int commandCount) {
        return restTemplate.getForEntity(String.format("http://%s:%s/commands?count={count}", host, port), CommandDataList.class, commandCount);
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
