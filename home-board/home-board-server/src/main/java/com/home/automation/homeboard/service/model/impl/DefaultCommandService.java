package com.home.automation.homeboard.service.model.impl;

import com.home.automation.homeboard.domain.ActionModel;
import com.home.automation.homeboard.domain.BoardModel;
import com.home.automation.homeboard.domain.CommandModel;
import com.home.automation.homeboard.domain.associations.CommandActionModel;
import com.home.automation.homeboard.exception.BaseModelNotFoundException;
import com.home.automation.homeboard.repo.CommandRepository;
import com.home.automation.homeboard.service.model.CommandService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional // spring transaction
public class DefaultCommandService implements CommandService {

    protected static final Logger logger = LogManager.getLogger(DefaultCommandService.class);

    @Autowired
    private CommandRepository commandRepository;

    @Override
    public Optional<CommandModel> createCommand(CommandModel command) {
        Assert.notNull(command, String.format("Command data %s to create should not be empty.", command));
        Assert.isNull(command.getId(), "Command id should not be provided"); // TODO: add controller validation, return BAD_REQUEST

        CommandModel commandModel = null;
        try {
            commandModel = commandRepository.saveCommand(command);
        }
        catch(Exception e) {
            // TODO: add custom errors that are treated appropriately
            logger.error(String.format("Cannot save command"), e.getMessage());
            throw createBaseModelNotFoundException("Connot save command", null);
        }
        return Optional.of(commandModel);
    }

    @Override
    public Optional<CommandModel> findCommandById(final Long commandId) {
        Assert.notNull(commandId, "Command id shouldn't be emtpy");
        return Optional.of(checkAndGetCommand(commandId));
    }

    @Override
    public Optional<CommandModel> deleteCommand(final Long commandId) {
        Assert.notNull(commandId, String.format("Command id %s to delete should not be empty.", commandId));

        final CommandModel commandModel = checkAndGetCommand(commandId);

        int deleteRows = commandRepository.removeCommand(commandId);
        return deleteRows > 0 ? Optional.of(commandModel ): Optional.empty();
    }

    @Override
    public Optional<CommandModel> updateCommand(final Long commandId, final CommandModel command) {
        Assert.notNull(commandId, "Command Id to update should not be null");
        Assert.notNull(command, "Command data to update should not be null");

        final CommandModel managedCommand = checkAndGetCommand(commandId);
        int updatedRecords = commandRepository.updateCommand(commandId, command);
        if (updatedRecords == 0) {
            throw new RuntimeException("could not update any records for id: " + commandId);
        }

        command.setCommandAction(managedCommand.getCommandAction());
        return Optional.of(command);
    }

    @Override
    public Optional<CommandModel> addActionsToCommand(Long commandId, List<Long> actionIds) {
        Assert.notNull(commandId, "Command Id to update should not be null");
        Assert.notNull(actionIds, "Command data to update should not be null");

        final CommandModel commandModel = checkAndGetCommand(commandId);

        // TODO: bulk insert pt actions
        final List<CommandActionModel> commandActionModels = commandRepository.addActionsToCommand(commandModel, actionIds);
        commandModel.getCommandAction().addAll(commandActionModels);

        return Optional.of(commandModel);
    }

    @Override
    public Optional<CommandModel> disableActionsForCommand(Long commandId, List<Long> actionIds, Boolean status) {
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

        return Optional.of(commandModel);
    }

    @Override
    // TODO: acum executia e per actiuni, nu per comenzi.. adauga si per comenzi adica mai multe actiuni in acelasi tim
    public MultiValueMap<BoardModel, ActionModel> splitActionsPerBoardFromCommand(final Long commandId) {
        final Optional<CommandModel> command = this.findCommandById(commandId);

        final MultiValueMap<BoardModel, ActionModel> boardToActionMap = new LinkedMultiValueMap<>();
        CollectionUtils.emptyIfNull(command.get().getCommandAction()).stream()
                .filter(CommandActionModel::getActive)
                .map(CommandActionModel::getAction)
                .forEach(action -> boardToActionMap.add(action.getBoard(), action));
        return boardToActionMap;
    }

    @Override
    public List<CommandModel> retrieveCommandsCount(int commandCount) {
        return commandRepository.retrieveCommandsCount(commandCount);
    }

    private CommandModel checkAndGetCommand(final Long commandId) {
        final Optional<CommandModel> command = commandRepository.findCommandById(commandId);
        command.orElseThrow(() -> createBaseModelNotFoundException(String.format("Command with id %s, doesn't exist.", commandId), commandId));
        return command.get();
    }

    private BaseModelNotFoundException createBaseModelNotFoundException(final String message, final Long commandId) {
        return new BaseModelNotFoundException(message, commandId, CommandModel.class);
    }
}
