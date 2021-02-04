package com.home.automation.users.client.impl;

import com.home.automation.users.client.UserClient;
import com.home.automation.users.client.exception.HomeUserClientException;
import com.home.automation.users.dto.UserData;
import com.home.automation.users.response.UserCreationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class DefaultUserClient implements UserClient {

    private static final Logger logger = LoggerFactory.getLogger(DefaultUserClient.class);

    private final RestTemplate restTemplate;

    private String host;
    private String port;

    public DefaultUserClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<UserData> findUserByUserName(String userName) throws HomeUserClientException {
        Assert.notNull(userName, "Username cannot be null");

        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            return restTemplate.exchange(String.format("http://%s:%s/user/%s", host, port, userName), HttpMethod.GET , new HttpEntity<>(headers), UserData.class);
        } catch (HttpClientErrorException.NotFound httpClientError) {
            logger.error(String.format("Username not found %s", userName));
            throw new HomeUserClientException(httpClientError.getStatusCode(), String.format("UserData %s doesn't exist", userName), httpClientError);

        } catch (HttpClientErrorException.Unauthorized httpClientError) {
            logger.error(String.format("Unauthorized 'storefront' ->  'userService' request. Reason: %s", httpClientError.getMessage()));
            throw new HomeUserClientException(httpClientError.getStatusCode(), String.format("Unauthorized user request, for %s", userName), httpClientError);

        } catch (Exception e) {
            // ex. : ResourceAccessException
            logger.error(String.format("Unauthorized 'storefront' ->  'userService' request. Reason: %s", e.getMessage()));
            final String errorMsg = String.format("Unknown error occured retrieving user %s", userName);
            throw new HomeUserClientException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage().isEmpty() ? errorMsg : e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<UserCreationResponse> createUser(final UserData user) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        try {
            return restTemplate.postForEntity(String.format("http://%s:%s/user", host, port), user, UserCreationResponse.class);
        } catch (Exception e) {
            logger.error(String.format("Error creating user", e.getMessage()));
            throw new HomeUserClientException(HttpStatus.INTERNAL_SERVER_ERROR, String.format("Error creating user [%s]", e.getMessage()), e);
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
