package com.home.automation.homeboard.repo;

import com.home.automation.homeboard.domain.ActionModel;
import com.home.automation.homeboard.domain.CommandModel;
import com.home.automation.homeboard.domain.associations.CommandActionModel;

import java.util.List;
import java.util.Optional;

public interface CommandRepository {

    CommandModel saveCommand(CommandModel commandModel);

    int updateCommand(Long commandId, CommandModel commandModel);

    int removeCommand(Long commandId);

    List<CommandActionModel> addActionsToCommand(CommandModel command, List<Long> actionIds);

    Optional<CommandModel> findCommandById(Long commandId);

    void changeCommandActionStatus(CommandModel commandId, List<ActionModel> actionIds, boolean activeStatus);

    List<CommandModel> retrieveCommandsCount(int commandCount);
}
