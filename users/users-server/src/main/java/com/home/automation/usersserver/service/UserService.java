package com.home.automation.usersserver.service;

import com.home.automation.users.dto.UserData;

public interface UserService {

    UserData getUserByUserName(String userName);

    UserData createUser(UserData userData);
}
