package com.storefront.repo;

import com.storefront.domain.LoginAttempt;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LoginAttemptRepo extends CrudRepository<LoginAttempt, String> {

    Optional<LoginAttempt> findByuid(String var1);
}
