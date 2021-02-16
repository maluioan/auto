package com.storefront.service.impl;


import com.storefront.domain.LoginAttempt;
import com.storefront.repo.LoginAttemptRepo;
import com.storefront.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class DefaultLoginService implements LoginService {

    @Autowired
    private LoginAttemptRepo loginAttemptRepo;

    @Override
    public LoginAttempt recordLoginAttempt(LoginAttempt loginAttempt) {
        Assert.notNull(loginAttempt, "Login attempt must not be null");
        return loginAttemptRepo.save(loginAttempt);
    }

    @Override
    public Optional<LoginAttempt> findLoginAttemptById(String loginAttemptId) {
        Assert.notNull(loginAttemptId, "Login attempt ID must not be null");
        return loginAttemptRepo.findByuid(loginAttemptId);
    }
}
