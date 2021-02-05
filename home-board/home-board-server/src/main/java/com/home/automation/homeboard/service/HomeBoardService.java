package com.home.automation.homeboard.service;

import com.home.automation.homeboard.converters.ActionConverter;
import com.home.automation.homeboard.converters.CommandConverter;
import com.home.automation.homeboard.data.CommandData;
import com.home.automation.homeboard.domain.CommandModel;
import com.home.automation.homeboard.exception.BaseModelNotFoundException;
import com.home.automation.homeboard.repo.CommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HomeBoardService {

    @Autowired
    private CommandConverter commandConverter;

    @Autowired
    private ActionConverter actionConverter;

    @Autowired
    private CommandRepository commandRepository;

    /**
     *
     * @param commandId
     * @return
     */
    public CommandData getCommandDataById(Long commandId) {
        Assert.notNull(commandId, "Command id shouldn't be emtpy");
        checkForCommand(commandId);

        return commandConverter.convertToData(commandRepository.findCommandById(commandId).get());
    }

    /**
     *
     * @param commandData
     * @return
     */
    public CommandData createCommand(final CommandData commandData) {
        Assert.notNull(commandData, String.format("Command data %s to create should not be empty.", commandData));

        final CommandModel commandModel = commandConverter.convertToModel(commandData);
        commandModel.setActive(Boolean.TRUE);
        final CommandModel newCommandModel = commandRepository.saveCommand(commandModel);
        return commandConverter.convertToData(newCommandModel);
    }

    /**
     *
     * @param commandId
     */
    public void deleteCommand(final Long commandId) {
        Assert.notNull(commandId, String.format("Command id %s to delete should not be empty.", commandId));
        checkForCommand(commandId);

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
        checkForCommand(commandId);

        final Optional<CommandModel> managedCommandOpt = commandRepository.findCommandById(commandId);
        final CommandModel newCommand = commandConverter.convertToModel(commandData);

        int updatedRecords = commandRepository.updateCommand(commandId, newCommand);
        if (updatedRecords == 0) {
            throw new RuntimeException("could not update any records for id: " + commandId);
        }
        newCommand.setCommandAction(managedCommandOpt.get().getCommandAction());
        return commandConverter.convertToData(newCommand);
    }

    public CommandData addActionsToCommand(final Long commandId, final List<Long> actions) {
        Assert.notNull(commandId, "Command Id to update should not be null");
        Assert.notNull(actions, "Command data to update should not be null");
        checkForCommand(commandId);

        // TODO: bulk insert pt actions
        final Optional<CommandModel> command = commandRepository.findCommandById(commandId);
        CommandModel commandModel = command.get();
        return null;

    }

    private void checkForCommand(final Long commandId) {
        final Optional<CommandModel> command = commandRepository.findCommandById(commandId);
        command.orElseThrow(() -> createBaseModelNotFoundException(String.format("Command with id %s, doesn't exist.", commandId), commandId));
    }

    private BaseModelNotFoundException createBaseModelNotFoundException(final String message, final Long commandId) {
        return new BaseModelNotFoundException(message, commandId, CommandModel.class);
    }
}
