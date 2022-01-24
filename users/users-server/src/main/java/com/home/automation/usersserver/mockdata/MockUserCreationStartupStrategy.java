package com.home.automation.usersserver.mockdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.automation.users.dto.UserData;
import com.home.automation.usersserver.service.UserService;
import com.home.automation.usersserver.startup.AbstractAppStartupStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockUserCreationStartupStrategy extends AbstractAppStartupStrategy {

    private static final Logger logger = LoggerFactory.getLogger(MockUserCreationStartupStrategy.class);

    @Autowired
    private UserService userService;

    @Override
    public void executeInternal() throws IOException {
        final UserData mockUser = retrieveMockUser();
        final UserData user = userService.getUserByUserName(mockUser.getUserName());

        if (user == null) {
            logger.info("*** Creating mock user");
            final UserData newUser = userService.createUser(mockUser);
            logger.info(String.format("*** Done creating user %s", newUser.getUserName()));
        }
    }

    private UserData retrieveMockUser() throws IOException {
        final InputStream resourceAsStream = MockUserCreationStartupStrategy.class.getClassLoader().getResourceAsStream("mock.json");
        return new ObjectMapper().readValue(resourceAsStream, UserData.class);
    }
}
