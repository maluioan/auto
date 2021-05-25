package com.storefront.service;

import com.home.automation.users.dto.UserData;
import com.storefront.data.UserAuthetificationDetails;
import com.storefront.register.CreateUserDataResultData;

public interface StoreFrontUserService {

    UserData getUserByUserName(String userName);

    CreateUserDataResultData createUser(UserData newUser);

    UserData getSessionUser();

    UserAuthetificationDetails getAuthentificatedUserDetails();
}
