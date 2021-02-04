package com.home.automation.homeboard.repo.impl;

import com.home.automation.homeboard.domain.ActionModel;
import com.home.automation.homeboard.repo.ActionRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class DefaultActionRepository extends AbstractBaseRepository implements ActionRepository {

    @Override
    public ActionModel saveOrUpdateAction(ActionModel actionModel) {
        entityManager.persist(actionModel);
        return actionModel;
    }

    @Override
    public ActionModel removeCommand(ActionModel commandModel) {
        return null;
    }
}
