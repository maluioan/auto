package com.home.automation.dispatcher.client;

import org.springframework.http.ResponseEntity;

public interface DispatcherClient {

    ResponseEntity<String> getDispatcherToken(String userName);
}
