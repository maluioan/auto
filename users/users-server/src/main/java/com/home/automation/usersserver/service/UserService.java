package com.home.automation.usersserver.service;

import com.home.automation.users.dto.UserData;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService {

    UserData getUserByUserName(String userName);

    UserData createUser(UserData userData);
}
