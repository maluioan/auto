package com.storefront.service.impl;

import com.home.automation.exception.HomeServiceLoginException;
import com.home.automation.users.client.UserClient;
import com.home.automation.users.client.exception.HomeUserClientException;
import com.home.automation.users.dto.UserData;
import com.home.automation.users.response.FieldErrorMessage;
import com.home.automation.users.response.UserCreationResponse;
import com.storefront.data.UserAuthetificationDetails;
import com.storefront.register.CreateUserDataResultData;
import com.storefront.service.SessionService;
import com.storefront.service.StoreFrontUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

// TODO: cum se comporta asta in mutithreaded context, incearca mai multe sesiuni
public class DefaultStoreFrontUserService implements StoreFrontUserService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultStoreFrontUserService.class);

    private UserClient userClient;

    private SessionService sessionService;

    @Override
    public UserData getUserByUserName(final String userName) {
        Assert.notNull(userName, "Username cannot be null");

        try {
            final ResponseEntity<UserData> userEntity = userClient.findUserByUserName(userName);

            return userEntity.getBody();
        } catch (HomeUserClientException httpClientError) {
            logger.error(String.format("Unauthorized 'storefront' ->  'userService' request. Reason: %s", httpClientError.getMessage()));
            throw new HomeServiceLoginException(httpClientError.getMessage(), httpClientError);
        }
    }

    @Override
    // TODO: refactor in fct de mesajele de eroare, dupa ce stiu ce ar trebui sa fac in cazuriile alea
    public CreateUserDataResultData createUser(UserData newUser) {
        Assert.notNull(newUser, "The user should not be null");

        final CreateUserDataResultData createdUserResult = new CreateUserDataResultData();
        try {
            final ResponseEntity<UserCreationResponse> userResponse = userClient.createUser(newUser);
            final UserCreationResponse creationResponse = userResponse.getBody();

            createdUserResult.setUser(creationResponse.getUser());
            createdUserResult.setErrorMessages(parseMessages(creationResponse));
        } catch (HomeUserClientException httpClientError) {
            logger.error(String.format("Unauthorized 'storefront' ->  'userService' request. Reason: %s", httpClientError.getMessage()));
            // TODO: add a localized message, remove exception
            // cum putem avea aici UserCreationResponse.fieldErrorMessageList?? (in caz de 409)
            createdUserResult.setErrorMessages(Arrays.asList("Upstream service error: ", httpClientError.getMessage()));
        } catch (Exception exc) {
            createdUserResult.setErrorMessages(Arrays.asList("An error occured: ", exc.getMessage()));
        }

        return createdUserResult;
    }

    @Override
    public UserData getSessionUser() {
        final String sessionUserName = getAuthentificatedUserDetails().getUsername();
        return this.getUserByUserName(sessionUserName);
    }

    @Override
    public UserAuthetificationDetails getAuthentificatedUserDetails() {
        return sessionService.getAuthentificatedUser();
    }

    private Collection<String> parseMessages(final UserCreationResponse creationResponse) {
        // TODO: add CollectionUtils.emptyIfNull()
        return (creationResponse.getFieldErrorMessageList() != null)
                ? creationResponse.getFieldErrorMessageList().stream().map(FieldErrorMessage::getMessage).collect(Collectors.toList())
                : Collections.emptyList();
    }

    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }
}
