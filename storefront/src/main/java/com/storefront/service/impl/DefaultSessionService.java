package com.storefront.service.impl;

import com.storefront.data.UserAuthetificationDetails;
import com.storefront.service.SessionService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DefaultSessionService implements SessionService {

    @Override
    public UserAuthetificationDetails getAuthentificatedUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication instanceof AnonymousAuthenticationToken ? getAnonymousUser() : (UserAuthetificationDetails) authentication.getPrincipal();
    }

    private UserAuthetificationDetails getAnonymousUser() {
        return new UserAuthetificationDetails("anonymous");
    }
}
