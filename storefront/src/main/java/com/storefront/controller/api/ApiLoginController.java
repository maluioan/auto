package com.storefront.controller.api;

import com.storefront.data.LoginData;
import com.storefront.domain.LoginAttempt;
import com.storefront.service.LoginService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ApiLoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/api/login/{id}")
    public ResponseEntity<LoginData> getLoginAttempt(@PathVariable(name = "id") Optional<String> loginAttemptUid) {
        final LoginData loginData = new LoginData();

        loginAttemptUid.map(loginService::findLoginAttemptById).ifPresent(loginAttempt ->
            loginAttempt.ifPresent(logAttempt -> convertLoginAttempt(logAttempt, loginData))
        );

        return loginData.isSuccess()
                ? new ResponseEntity<>(loginData, HttpStatus.OK)
                : new ResponseEntity<>(loginData, HttpStatus.UNAUTHORIZED);
    }

    // TODO: add mapper
    private LoginData convertLoginAttempt(final LoginAttempt loginAttempt, LoginData loginData) {
        loginData.setFailureReason(loginAttempt.getFailureMessage());
        loginData.setSuccess(BooleanUtils.isTrue(loginAttempt.getSuccess()));
        loginData.setUsedClientId(loginAttempt.getUsedClientId());
        return null;
    }
}
