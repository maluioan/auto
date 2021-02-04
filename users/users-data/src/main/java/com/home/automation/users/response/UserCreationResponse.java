package com.home.automation.users.response;

import com.home.automation.users.dto.UserData;

import java.util.List;

public class UserCreationResponse {

    private UserData user;
    private List<FieldErrorMessage> fieldErrorMessageList;

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public List<FieldErrorMessage> getFieldErrorMessageList() {
        return fieldErrorMessageList;
    }

    public void setFieldErrorMessageList(List<FieldErrorMessage> fieldErrorMessageList) {
        this.fieldErrorMessageList = fieldErrorMessageList;
    }
}
