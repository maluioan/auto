package com.storefront.login.handler;

import com.home.automation.util.CommonUtils;
import com.storefront.data.UserAuthetificationDetails;
import com.storefront.domain.LoginAttempt;
import com.storefront.service.DispatcherService;
import com.storefront.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.client.ResourceAccessException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomeUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(HomeUrlAuthenticationSuccessHandler.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private DispatcherService dispatcherService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.onAuthenticationSuccess(request, response, authentication);

        final Object principal = authentication.getPrincipal();
        if (principal instanceof UserAuthetificationDetails) {
            final UserAuthetificationDetails uad = (UserAuthetificationDetails)principal;
            fetchAndSetWsDispatcherToken(uad);
            createLoginAttempt(uad);
        }
    }

    private void fetchAndSetWsDispatcherToken(final UserAuthetificationDetails uad) {
        try {
            final String dispatcherToken = dispatcherService.getDispatcherToken(uad.getUsername());
            uad.setWsToken(dispatcherToken);
        } catch (ResourceAccessException rae) {
            logger.warn("Error fetching ws token, cannot continue.");
        }
    }

    protected void createLoginAttempt(final UserAuthetificationDetails authentication) {
        final LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setUid(CommonUtils.createRandomSixDigitsId());
        loginAttempt.setSuccess(Boolean.TRUE);
        loginAttempt.setUsedClientId(authentication.getUsername());

        loginService.recordLoginAttempt(loginAttempt);
    }
}
