package com.home.automation.homeboard.repo.impl;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Deprecated
public abstract class AbstractBaseRepository {

    @PersistenceContext
    protected EntityManager entityManager;

}
