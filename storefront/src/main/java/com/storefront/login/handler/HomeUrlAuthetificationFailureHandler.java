package com.storefront.login.handler;

import com.home.automation.users.client.exception.HomeUserClientException;
import com.home.automation.util.CommonUtils;
import com.storefront.domain.LoginAttempt;
import com.storefront.login.data.FailureReason;
import com.storefront.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

public class HomeUrlAuthetificationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(HomeUrlAuthetificationFailureHandler.class);

    @Autowired
    private LoginService loginService;
    // TODO: this already exists in upper class, find another solution
    private String defaultFailureUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        logger.info("Failure logging: " + e.getCause());

        final FailureReason failureReason = findFailureReason(e);
        final LoginAttempt loginAttempt = storeLoginFailure(httpServletRequest, e, failureReason);
        this.getRedirectStrategy().sendRedirect(httpServletRequest, httpServletResponse, String.format(this.defaultFailureUrl + "/%s", loginAttempt.getUid()));
    }

    @Override
    public void setDefaultFailureUrl(String defaultFailureUrl) {
        super.setDefaultFailureUrl(defaultFailureUrl);
        this.defaultFailureUrl = defaultFailureUrl;
    }

    private LoginAttempt storeLoginFailure(HttpServletRequest httpServletRequest, AuthenticationException e, FailureReason failureReason) {
        final LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setUid(CommonUtils.createRandomSixDigitsId());
        loginAttempt.setFailureReason(failureReason);
        loginAttempt.setFailureMessage(e.getMessage());
        loginAttempt.setSuccess(Boolean.FALSE);
        // TODO: pune parameteri 'par' si 'util' in acelasi loc, se mai folosesc si la 'form-login'
        loginAttempt.setUsedClientId(httpServletRequest.getParameter("par"));
        loginAttempt.setUsedClientSecret(httpServletRequest.getParameter("util"));

        return loginService.recordLoginAttempt(loginAttempt);
    }

    private FailureReason findFailureReason(final Throwable exc) {
        FailureReason reason = FailureReason.UNKNOWN;

        if (isCausedBy(exc, DisabledException.class)) {
            reason = FailureReason.DISABLED;

        } else if (isCausedBy(exc, BadCredentialsException.class)) {
            reason = FailureReason.BAD_CREDENTIALS;

        } else if (isCausedBy(exc, CredentialsExpiredException.class)) {
            reason = FailureReason.CREDENTIALS_EXPIRED;

        } else if (isCausedBy(exc, HomeUserClientException.class)) {
            reason = findHttpClientErrorException(exc);

        } else if (isCausedBy(exc, InternalAuthenticationServiceException.class)) {
            reason = FailureReason.INTERNAL_ERROR;
        }
        return reason;
    }

    private FailureReason findHttpClientErrorException(Throwable exc) {
        FailureReason reason = FailureReason.INTERNAL_ERROR;

        Throwable th = exc;
        while (!(th instanceof HomeUserClientException)) {
            th = th.getCause();
        }
        final HomeUserClientException huse = (HomeUserClientException) th;

        final HttpStatus statusCode = huse.getStatusCode();
        if (HttpStatus.UNAUTHORIZED.equals(statusCode)) {
            reason = FailureReason.UNAUTHORIZED;

        } else if (HttpStatus.NOT_FOUND.equals(statusCode)) {
            reason = FailureReason.NOT_FOUND;

        }
        return reason;
    }

    private <T extends Exception> boolean isCausedBy(final Throwable exc, final Class<T> causedBy) {
        if (exc.getClass().isAssignableFrom(causedBy)) {
            return true;
        } else if (exc.getCause() != null) {
            return isCausedBy(exc.getCause(), causedBy);
        }
        return false;
    }

    // TODO: put this in a common place
    private String createLoginAttemptUid() {
        final Random rnd = new Random();
        return String.valueOf(rnd.nextInt(999999));
    }
}
