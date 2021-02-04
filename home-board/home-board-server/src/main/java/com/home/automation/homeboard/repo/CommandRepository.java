package com.home.automation.homeboard.repo;

import com.home.automation.homeboard.domain.ActionModel;
import com.home.automation.homeboard.domain.CommandModel;

import java.util.List;
import java.util.Optional;

public interface CommandRepository {

    CommandModel saveCommand(CommandModel commandModel);

    int updateCommand(Long commandId, CommandModel commandModel);

    void removeCommand(Long commandId);

    @Deprecated
    Optional<CommandModel> addActionsToCommand(CommandModel commandModel, List<ActionModel> actions);

    Optional<CommandModel> findCommandById(Long commandId);
}
