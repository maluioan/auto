package com.storefront.controller;

import com.home.automation.users.dto.UserData;
import com.storefront.service.DispatcherService;
import com.storefront.service.StoreFrontUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@ConfigurationProperties(prefix = "server.handler")
public class StoreFrontController {
    private String host;
    private String port;

    @Autowired
    private StoreFrontUserService storeFrontUserService;

    @Autowired
    private DispatcherService dispatcherService;

    @GetMapping
    public String home(final Model model) {
        final UserData sessionUser = storeFrontUserService.getSessionUser();
        final String userName = sessionUser.getUserName();
        model.addAttribute("userName",userName );
        model.addAttribute("host", getHost());
        model.addAttribute("port", getPort());
        model.addAttribute("dispatcherToken", dispatcherService.getDispatcherToken(userName));
        return "home";
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public StoreFrontUserService getStoreFrontUserService() {
        return storeFrontUserService;
    }
}

