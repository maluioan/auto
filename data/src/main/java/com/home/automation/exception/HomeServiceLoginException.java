package com.home.automation.exception;

/**
 * Used internally among services
 */
public class HomeServiceLoginException extends RuntimeException{

    private Integer status;

    public HomeServiceLoginException(String message, Exception e) {
        this(message, e, null);
    }

    public HomeServiceLoginException(String message, Throwable cause, Integer status) {
        super(message, cause);
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
