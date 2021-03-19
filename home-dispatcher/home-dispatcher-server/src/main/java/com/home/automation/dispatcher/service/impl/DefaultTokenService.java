package com.home.automation.dispatcher.service.impl;

import com.home.automation.dispatcher.client.data.TokenData;
import com.home.automation.dispatcher.repository.TokenRepository;
import com.home.automation.dispatcher.service.TokenService;
import com.home.automation.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
// TODO: creeaza un serviciu serios aici
public class DefaultTokenService implements TokenService {

    @Autowired
    private TokenRepository tokenRepo;

    @Override
    public String createToken(final String userName) {
        final String randomSixDigitsId = CommonUtils.createRandomSixDigitsId();
        final String timestamp = String.valueOf(System.currentTimeMillis());
        // TODO: create an MD5 encoded token, salted with user name
        final String token = String.format(randomSixDigitsId + "-" + userName + "-" + timestamp);
        tokenRepo.storeToken(token);
        return token;
    }

    @Override
    public TokenData findToken(final String token) {
        Assert.notNull(token, "Token to parse cannot be null");
        final String[] tokens = token.split("-");

        // TODO: treat invalid tokens
        TokenData td = new TokenData();
        td.setToken(token);
        td.setUserName(tokens[1]);
        return td;
    }
}
