package com.storefront.service;

import com.storefront.data.UserAuthetificationDetails;

public interface SessionService {

    UserAuthetificationDetails getAuthentificatedUser();
}
