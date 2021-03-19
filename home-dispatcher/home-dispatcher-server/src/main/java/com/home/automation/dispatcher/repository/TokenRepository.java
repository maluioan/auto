package com.home.automation.dispatcher.repository;

import com.home.automation.dispatcher.client.data.TokenData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
// TODO: implement
public class TokenRepository {

    private Map<String, TokenData> tokens = new HashMap<>();

    public void storeToken(String token) {
        final String[] tokens = token.split("-");

        // TODO: treat invalid tokens
        TokenData td = new TokenData();
        td.setToken(token);
        td.setUserName(tokens[1]);

        this.tokens.put(token, td);
    }

    // TODO: get TokenModel based on token
    public TokenData getToken(String token) {
        return tokens.get(token);
    }
}
