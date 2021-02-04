package com.home.automation.users.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class HomeUserClientException extends HttpClientErrorException {
    public HomeUserClientException(HttpStatus statusCode, String statusText, Throwable throwable) {
        super(statusCode, statusText);
        super.initCause(throwable);
    }
}
