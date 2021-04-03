package com.home.automation.homeboard.facade.impl;

import com.home.automation.homeboard.converters.CommandConverter;
import com.home.automation.homeboard.data.CommandData;
import com.home.automation.homeboard.domain.CommandModel;
import com.home.automation.homeboard.facade.CommandFacade;
import com.home.automation.homeboard.service.model.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DefaultCommandFacade implements CommandFacade {

    @Autowired
    private CommandConverter commandConverter;

    @Autowired
    private CommandService commandService;

    @Override
    public CommandData createCommand(final CommandData command) {
        final CommandModel commandModel = commandConverter.convertToModel(command);
        return commandService.createCommand(commandModel).map(commandConverter::convertToData).get(); // .orElseThrow()
    }

    @Override
    public CommandData findCommandById(final Long commandId) {
        return commandService.findCommandById(commandId).map(commandConverter::convertToData).get();
    }

    @Override
    public CommandData deleteCommand(Long commandId) {
        final Optional<CommandModel> commandModel = commandService.deleteCommand(commandId);
        return commandModel.isPresent() ? commandModel.map(commandConverter::convertToData).get() : null;
    }

    @Override
    public CommandData updateCommand(Long commandId, CommandData command) {
        final CommandModel commandModel = commandConverter.convertToModel(command);
        return commandService.updateCommand(commandId, commandModel).map(commandConverter::convertToData).get();
    }

    @Override
    public CommandData addActionsToCommand(Long commandId, List<Long> actions) {
        final Optional<CommandModel> commandModel = commandService.addActionsToCommand(commandId, actions);
        return commandModel.map(commandConverter::convertToData).get();
    }

    @Override
    public CommandData disableActionsForCoomand(Long commandId, List<Long> actionIds, Boolean status) {
        return commandService.disableActionsForCommand(commandId, actionIds, status).map(commandConverter::convertToData).get();
    }
}
