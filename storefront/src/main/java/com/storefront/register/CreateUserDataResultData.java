package com.storefront.register;

import com.home.automation.users.dto.UserData;

import java.util.Collection;

public class CreateUserDataResultData extends UserData {

    private UserData user;
    private Collection<String> errorMessages;

    public void setUser(UserData user) {
        this.user = user;
    }

    public UserData getUser() {
        return user;
    }

    public void setErrorMessages(Collection<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public Collection<String> getErrorMessages() {
        return errorMessages;
    }
}
