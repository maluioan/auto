package com.home.automation.homeboard.facade;

import com.home.automation.homeboard.data.CommandData;
import com.home.automation.homeboard.data.CommandDataList;

import java.util.List;

public interface CommandFacade {

    CommandData createCommand(CommandData command);

    CommandData findCommandById(Long commandId);

    CommandData deleteCommand(Long commandId);

    CommandData updateCommand(Long commandId, CommandData command);

    CommandData addActionsToCommand(final Long commandId, final List<Long> actions);

    CommandData disableActionsForCoomand(final Long commandId, final List<Long> actionIds, Boolean status);

    CommandDataList findCommandsCount(int commandCount);
}
