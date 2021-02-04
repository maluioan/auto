package com.home.automation.util.initializer;

import com.home.automation.util.auth.strategy.HomeBasicAuthStrategy;
import com.home.automation.util.auth.strategy.ServiceAuthStrategy;
import org.springframework.http.client.*;

public class HomeClientHttpRequestInitializer implements ClientHttpRequestInitializer {

    private static final ServiceAuthStrategy DEFAULT_AUTHETIFICATION = new HomeBasicAuthStrategy();

    private ServiceAuthStrategy authStrategy;
    private final String clientId;
    private final String clientSecret;

    public HomeClientHttpRequestInitializer(final String clientId, final String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public void initialize(ClientHttpRequest clientHttpRequest) {
        getAuthStrategy().addAuthentification(clientHttpRequest, clientId + "m", clientSecret + "c");
    }

    public ServiceAuthStrategy getAuthStrategy() {
        return (authStrategy != null) ? authStrategy : HomeClientHttpRequestInitializer.DEFAULT_AUTHETIFICATION;
    }

    public void setAuthStrategy(ServiceAuthStrategy authStrategy) {
        this.authStrategy = authStrategy;
    }

}
