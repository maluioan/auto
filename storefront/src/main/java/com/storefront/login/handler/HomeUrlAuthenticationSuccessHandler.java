package com.storefront.login.handler;

import com.home.automation.util.CommonUtils;
import com.storefront.domain.LoginAttempt;
import com.storefront.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomeUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private LoginService loginService;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.onAuthenticationSuccess(request, response, authentication);

        final LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setUid(CommonUtils.createRandomSixDigitsId());
        loginAttempt.setSuccess(Boolean.TRUE);
        loginAttempt.setUsedClientId(authentication.getName());

        loginService.recordLoginAttempt(loginAttempt);
    }
}
