package com.home.automation.usersserver.service.impl;

import com.home.automation.users.dto.UserData;
import com.home.automation.usersserver.converters.UserConverter;
import com.home.automation.usersserver.domain.UserModel;
import com.home.automation.usersserver.repo.UserRepository;
import com.home.automation.usersserver.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import javax.persistence.NonUniqueResultException;
import java.util.List;

// TODO: nu amesteca auto si injection
public class HomeUserService implements UserService {

    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserData getUserByUserName(String userName) {
        Assert.notNull(userName, "UserName should not be null");

        final List<UserModel> usersByUserName = userRepository.findByUserName(userName);
        if (CollectionUtils.size(usersByUserName) > 1) {
            // TODO: add custom exception
            throw new NonUniqueResultException(String.format("Multiple users found for user anem %s", userName));
        }
        return (usersByUserName.size() == 1) ? convertUserToData(usersByUserName.get(0)) : null;
    }

    @Override
    public UserData createUser(final UserData userData) {
        try {
            final UserModel newUserModel = userRepository.save(convertDataToUser(userData));
            return convertUserToData(newUserModel);
        } catch (Exception e ) {
            // TODO: add custom exception
            throw new RuntimeException("", e);
        }
    }

    private UserData convertUserToData(final UserModel userModel) {
        return userConverter.convertToData(userModel);
    }

    private UserModel convertDataToUser(final UserData userData) {
        userData.setPassword(passwordEncoder.encode("test")); // TODO: remove
        return userConverter.convertToModel(userData);
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setUserConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }
}
