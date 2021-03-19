package com.storefront.controller;

import com.storefront.domain.LoginAttempt;
import com.storefront.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping({"/login", "/login/{id}"})
    public String login(@PathVariable(name = "id") Optional<String> uid,
                        final Model model) {

        if (uid.isPresent()) {
            final Optional<LoginAttempt> loginAttempt = loginService.findLoginAttemptById(uid.get());

            if (loginAttempt.isPresent()) {
                model.addAttribute("failureReason", loginAttempt.get().getFailureReason());
            }
        }
        return "login";
    }
}
