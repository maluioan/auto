package com.home.automation.dispatcher.service;

import com.home.automation.dispatcher.client.data.TokenData;

public interface TokenService {

    String createToken(final String userName);

    TokenData findToken(String token);

}
