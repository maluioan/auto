package com.home.automation.util.auth.strategy;

import org.springframework.http.HttpRequest;

public interface ServiceAuthStrategy {

    void addAuthentification(final HttpRequest httpRequest, final String clientName, final String clientSecret);

}