package com.home.automation.util.auth.strategy;

import org.springframework.http.HttpRequest;
import org.springframework.util.Assert;

import java.nio.charset.Charset;

public class HomeBasicAuthStrategy implements ServiceAuthStrategy {

    @Override
    public void addAuthentification(HttpRequest httpRequest, String clientName, String clientSecret) {
        Assert.notNull(httpRequest, "Http Request should not be null");
        Assert.notNull(clientName, "clientName should not be null");
        Assert.notNull(clientSecret, "clientSecret should not be null");

        httpRequest.getHeaders().setBasicAuth(clientName, clientSecret, Charset.defaultCharset());
    }
}
