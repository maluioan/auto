package com.home.automation.users.client;

import com.home.automation.users.client.exception.HomeUserClientException;
import com.home.automation.users.dto.UserData;
import com.home.automation.users.response.UserCreationResponse;
import org.springframework.http.ResponseEntity;

public interface UserClient {

    ResponseEntity<UserData> findUserByUserName(String userName) throws HomeUserClientException;

    ResponseEntity<UserCreationResponse> createUser(UserData user) throws HomeUserClientException;
}
