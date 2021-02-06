package com.home.automation.homeboard.service;

import com.home.automation.homeboard.converters.ActionConverter;
import com.home.automation.homeboard.converters.CommandConverter;
import com.home.automation.homeboard.data.ActionData;
import com.home.automation.homeboard.data.CommandData;
import com.home.automation.homeboard.domain.ActionModel;
import com.home.automation.homeboard.domain.CommandModel;
import com.home.automation.homeboard.domain.associations.CommandActionModel;
import com.home.automation.homeboard.exception.BaseModelNotFoundException;
import com.home.automation.homeboard.repo.ActionRepository;
import com.home.automation.homeboard.repo.CommandRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class HomeBoardService {

    @Autowired
    private CommandConverter commandConverter;

    @Autowired
    private ActionConverter actionConverter;

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private ActionRepository actionRepository;

    /**
     *
     * @param commandId
     * @return
     */
    public CommandData findCommandById(Long commandId) {
        Assert.notNull(commandId, "Command id shouldn't be emtpy");

        return commandConverter.convertToData(checkAndGetCommand(commandId));
    }

    /**
     *
     * @param commandData
     * @return
     */
    public CommandData createCommand(final CommandData commandData) {
        Assert.notNull(commandData, String.format("Command data %s to create should not be empty.", commandData));
        Assert.isNull(commandData.getId(), "Command id should not be provided"); // TODO: add controller validation, return BAD_REQUEST

        final CommandModel commandModel = commandConverter.convertToModel(commandData);
        final CommandModel newCommandModel = commandRepository.saveCommand(commandModel);
        return commandConverter.convertToData(newCommandModel);
    }

    /**
     *
     * @param commandId
     */
    public void deleteCommand(final Long commandId) {
        Assert.notNull(commandId, String.format("Command id %s to delete should not be empty.", commandId));
        checkAndGetCommand(commandId);

        commandRepository.removeCommand(commandId);
    }

    /**
     *
     * @param commandId
     * @param commandData
     * @return
     */
    public CommandData updateCommand(final Long commandId, final CommandData commandData) {
        Assert.notNull(commandId, "Command Id to update should not be null");
        Assert.notNull(commandData, "Command data to update should not be null");
        final CommandModel managedCommand = checkAndGetCommand(commandId);

        final CommandModel newCommand = commandConverter.convertToModel(commandData);

        int updatedRecords = commandRepository.updateCommand(commandId, newCommand);
        if (updatedRecords == 0) {
            throw new RuntimeException("could not update any records for id: " + commandId);
        }
        newCommand.setCommandAction(managedCommand.getCommandAction());
        return commandConverter.convertToData(newCommand);
    }

    /**
     *
     * @param commandId
     * @param actions
     * @return
     */
    public CommandData addActionsToCommand(final Long commandId, final List<Long> actions) {
        Assert.notNull(commandId, "Command Id to update should not be null");
        Assert.notNull(actions, "Command data to update should not be null");

        final CommandModel commandModel = checkAndGetCommand(commandId);
        // TODO: bulk insert pt actions
        final List<CommandActionModel> commandActionModels = commandRepository.addActionsToCommand(commandModel, actions);
        commandModel.getCommandAction().addAll(commandActionModels);
        return commandConverter.convertToData(commandModel);
    }

    public CommandData disableActionsForCoomand(final Long commandId, final List<Long> actionIds, Boolean status) {
        Assert.notNull(commandId, "Command Id to update should not be null");
        Assert.notNull(actionIds, "Command data to update should not be null");
        Assert.notNull(status, "Invalid active status");

        final List<Long> actIds = new ArrayList<>(actionIds);

        final CommandModel commandModel = checkAndGetCommand(commandId);

        final Set<CommandActionModel> commandAction = commandModel.getCommandAction();
        final List<ActionModel> actionModels = commandAction.stream()
                .filter(ca -> actIds.removeIf(actId -> actId.equals(ca.getAction().getId())))
                .map(ca -> {
                    ca.setActive(status);
                    return ca.getAction();
                })
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(actIds)) {
            throw new RuntimeException(String.format("Actions with id %s not found for command %s", actIds, commandId));
        }

        commandRepository.changeCommandActionStatus(commandModel, actionModels, status);
        return commandConverter.convertToData(commandModel);
    }

    private ActionModel findAssociatedCommandAction(Long commandId, final Long actionId, Set<CommandActionModel> commandAction) {
        final Optional<ActionModel> commandActionOpt = commandAction.stream()
                .map(CommandActionModel::getAction)
                .filter(action -> Objects.equals(action.getId(), actionId))
                .findFirst();
        return commandActionOpt.orElseThrow(() -> new RuntimeException(String.format("Action with id %s not found for command %s", actionId, commandId)));
    }

    public CommandData enableActionsForCoomand(final Long commandId, final List<Long> actions) {
        Assert.notNull(commandId, "Command Id to update should not be null");
        Assert.notNull(actions, "Command data to update should not be null");

        final CommandModel commandModel = checkAndGetCommand(commandId);
        // TODO: bulk insert pt actions
        final List<CommandActionModel> commandActionModels = commandRepository.addActionsToCommand(commandModel, actions);
        commandModel.getCommandAction().addAll(commandActionModels);
        return commandConverter.convertToData(commandModel);
    }

    /**
     *
     * @param actionId
     */
    public ActionData findActionById(Long actionId) {
        Assert.notNull(actionId, "Action id shouldn't be emtpy");

        return actionConverter.convertToData(checkAndGetAction(actionId));
    }

    public ActionData createAction(final ActionData actionData) {
        Assert.notNull(actionData,"Action should not be empty");
        Assert.isNull(actionData.getId(), "Action id should not be null"); // TODO: add controller validation, and return BAD_REQUEST

        final ActionModel actionModel = actionConverter.convertToModel(actionData);
        final ActionModel newActionModel = this.actionRepository.saveAction(actionModel);
        return actionConverter.convertToData(newActionModel);
    }

    public ActionData updateAction(Long actionId, ActionData actionData) {
        Assert.notNull(actionData,"Action should not be empty");
        checkAndGetAction(actionId);

        final ActionModel newActionModel = actionConverter.convertToModel(actionData);
        int updatedActions = actionRepository.updateAction(actionId, newActionModel);
        if (updatedActions == 0) {
            throw new RuntimeException("could not update any records for id: " + updatedActions);
        }
        return actionConverter.convertToData(newActionModel);
    }

    public void deleteAction(final Long actionId) {
        Assert.notNull(actionId, String.format("Action id %s to delete should not be empty.", actionId));
        checkAndGetAction(actionId);

        actionRepository.removeAction(actionId);
    }

    private CommandModel checkAndGetCommand(final Long commandId) {
        final Optional<CommandModel> command = commandRepository.findCommandById(commandId);
        command.orElseThrow(() -> createBaseModelNotFoundException(String.format("Command with id %s, doesn't exist.", commandId), commandId));
        return command.get();
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
