package com.home.automation.usersserver.repo;

import com.home.automation.usersserver.domain.UserModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserModel, String> {

    List<UserModel> findByUserName(String userName);

}