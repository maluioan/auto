package com.storefront.controller.api;

import com.storefront.data.StateData;
import com.storefront.data.UserAuthetificationDetails;
import com.storefront.service.StoreFrontUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationStatsController {

    @Autowired
    private StoreFrontUserService storeFrontUserService;

    @GetMapping("/api/stats")
    public StateData findApplicationStats() {
        final UserDetails authentificatedUser = storeFrontUserService.getAuthentificatedUserDetails();

        final StateData sd = new StateData();
        sd.setLoggedIn(!StringUtils.equals(authentificatedUser.getUsername(), "anonymous"));
        sd.setToken(((UserAuthetificationDetails) authentificatedUser).getWsToken());
        sd.setUserName(authentificatedUser.getUsername());

        return sd;
    }
}
