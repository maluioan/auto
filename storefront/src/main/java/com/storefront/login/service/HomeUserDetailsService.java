package com.storefront.login.service;

import com.home.automation.exception.HomeServiceLoginException;
import com.home.automation.users.dto.UserData;
import com.storefront.service.StoreFrontUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collection;
import java.util.Collections;

public class HomeUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(HomeUserDetailsService.class);

    private StoreFrontUserService userService;

    @Override
    public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
        try {
            final UserData user = userService.getUserByUserName(userName);
            if (user == null) {
                throw new UsernameNotFoundException(String.format("UserData %s not found", user));
            }

            return createUserDetailsForUser(user);
        } catch (HttpClientErrorException.NotFound httpClientError) {
            logger.error(String.format("Username not found %s", userName));
            throw new HomeServiceLoginException(String.format("UserData %s doesn't exist", userName), httpClientError);

        } catch (HttpClientErrorException.Unauthorized httpClientError) {
            logger.error(String.format("Unauthorized 'storefront' ->  'userService' request. Reason: %s", httpClientError.getMessage()));
            throw new HomeServiceLoginException(String.format("UserData %s doesn't exist", userName), httpClientError);
        }
    }

    private UserDetails createUserDetailsForUser(final UserData homeUser) {
        return new org.springframework.security.core.userdetails.User(homeUser.getUserName(), homeUser.getPassword(), homeUser.getActive(), true, true, true, findRoles(homeUser));
    }

    private Collection<? extends GrantedAuthority> findRoles(UserData body) {
        return Collections.emptyList();
    }

    public void setUserService(StoreFrontUserService userService) {
        this.userService = userService;
    }
}
