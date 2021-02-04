package com.storefront.service;

import com.storefront.domain.LoginAttempt;

import java.util.Optional;

public interface LoginService {

    LoginAttempt recordLoginAttempt(LoginAttempt loginAttempt);

    Optional<LoginAttempt> findLoginAttemptById(String loginAttemptId);
}
