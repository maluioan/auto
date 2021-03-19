package com.home.automation.dispatcher.controller;

import com.home.automation.dispatcher.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
// TODO: add security pt rest apis, daca nu exista deja
public class DispatcherTokenController {

    @Autowired
    private TokenService tokenService;

    @GetMapping(value = "/dispatch/token")
    public ResponseEntity<String> createToken(@RequestParam(value = "user", required = false) final String userName) {
        ResponseEntity<String> response = null;
        if (StringUtils.isNotEmpty(userName)) {
            final String token = tokenService.createToken(userName);
            response = new ResponseEntity<>(token, HttpStatus.OK);

        } else {
            response = new ResponseEntity<>("Please specify a name", HttpStatus.BAD_REQUEST);
        }

        return response;
    }
}
