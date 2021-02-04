package com.home.automation.usersserver.controller;

import com.home.automation.users.dto.UserData;
import com.home.automation.users.response.FieldErrorMessage;
import com.home.automation.users.response.UserCreationResponse;
import com.home.automation.usersserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NonUniqueResultException;
import java.util.Arrays;

@RestController
// TODO: maybe add an interface for the controller and the util as a contract?
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @GetMapping(value = "/user/{userName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserData> findUserByUserName(@PathVariable(value = "userName") String userName) {
        ResponseEntity response = new ResponseEntity(HttpStatus.NOT_FOUND);
        try {
            final UserData userData = userService.getUserByUserName(userName);
            if (userData != null) {
                response = new ResponseEntity(userData, HttpStatus.OK);
            }
        } catch (NonUniqueResultException nure) {
            response = new ResponseEntity(String.format("Multiple users found for user %s", userName), HttpStatus.EXPECTATION_FAILED);
        }
        return response;
    }

    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createUser(@RequestBody UserData user) {

        final UserCreationResponse userCreationResponse = new UserCreationResponse();
        try {
            userCreationResponse.setUser(userService.createUser(user));
            return new ResponseEntity<>(userCreationResponse, HttpStatus.OK);
        } catch (DataIntegrityViolationException dive) {
            // TODO: return response cu ce fielduri nu sunt ok
            userCreationResponse.setFieldErrorMessageList(Arrays.asList(new FieldErrorMessage("username", "Existing username")));
            return new ResponseEntity<>(userCreationResponse, HttpStatus.CONFLICT);
        }
    }
}
