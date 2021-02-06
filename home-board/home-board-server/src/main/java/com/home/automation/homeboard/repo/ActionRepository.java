package com.home.automation.homeboard.repo;

import com.home.automation.homeboard.domain.ActionModel;

import java.util.Optional;

public interface ActionRepository {

    ActionModel saveAction(ActionModel commandModel);

    ActionModel removeAction(Long actionId);

    Optional<ActionModel> findActionById(Long actionId);

    int updateAction(Long actionId, ActionModel actionModel);
}
