package com.home.automation.homeboard.service.model.impl;

import com.home.automation.homeboard.domain.ActionModel;
import com.home.automation.homeboard.domain.CommandModel;
import com.home.automation.homeboard.exception.BaseModelNotFoundException;
import com.home.automation.homeboard.repo.ActionRepository;
import com.home.automation.homeboard.service.model.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
@Transactional
public class DefaultActionService implements ActionService {

    @Autowired
    private ActionRepository actionRepository;

    @Override
    public Optional<ActionModel> createAction(final ActionModel action) {
        Assert.notNull(action, "Action should not be empty");
        Assert.isNull(action.getId(), "Action id should not be null"); // TODO: add controller validation, and return BAD_REQUEST

        return Optional.of(this.actionRepository.saveAction(action));
    }

    @Override
    public Optional<ActionModel> findActionById(final Long actionId) {
        Assert.notNull(actionId, "Action id shouldn't be emtpy");

        return Optional.of(checkAndGetAction(actionId));
    }

    @Override
    public Optional<ActionModel> updateAction(final Long actionId, final ActionModel newAction) {
        Assert.notNull(newAction, "Action should not be empty");
        checkAndGetAction(actionId);

        int updatedActions = actionRepository.updateAction(actionId, newAction);
        if (updatedActions == 0) {
            throw new RuntimeException("could not update any records for id: " + updatedActions);
        }
        return Optional.of(newAction);
    }

    @Override
    public Optional<ActionModel> deleteAction(final Long actionId) {
        Assert.notNull(actionId, String.format("Action id %s to delete should not be empty.", actionId));

        final ActionModel actionModel = checkAndGetAction(actionId);
        actionRepository.removeAction(actionId);

        return Optional.of(actionModel);
    }

    private ActionModel checkAndGetAction(final Long actionId) {
        final Optional<ActionModel> command = actionRepository.findActionById(actionId);
        command.orElseThrow(() -> createBaseModelNotFoundException(String.format("Action with id %s, doesn't exist.", actionId), actionId));
        return command.get();
    }

    private BaseModelNotFoundException createBaseModelNotFoundException(final String message, final Long commandId) {
        return new BaseModelNotFoundException(message, commandId, CommandModel.class);
    }

}
