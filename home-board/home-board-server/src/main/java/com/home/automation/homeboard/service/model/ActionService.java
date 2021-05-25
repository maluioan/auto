package com.home.automation.homeboard.service.model;

import com.home.automation.homeboard.domain.ActionModel;

import java.util.Optional;

public interface ActionService {

    Optional<ActionModel> createAction(ActionModel action);

    Optional<ActionModel> findActionById(Long actionId);

    Optional<ActionModel> findExecutorById(final String executionId);

    Optional<ActionModel> updateAction(Long actionId, ActionModel action);

    Optional<ActionModel> deleteAction(final Long actionId);
}
