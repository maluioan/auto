package com.home.automation.dispatcher.client.data;

import java.util.Date;

public class TokenData {
    private String token;
    private String userName;
//    private Date creationDate;

    // TODO: add validity dates


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
