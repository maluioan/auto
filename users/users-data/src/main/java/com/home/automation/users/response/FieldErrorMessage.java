package com.home.automation.users.response;

public class FieldErrorMessage {
    private final String field;
    private final String message;

    public FieldErrorMessage(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

}
