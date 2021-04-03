package com.home.automation.homeboard.service.model;

import com.home.automation.homeboard.domain.ActionModel;
import com.home.automation.homeboard.domain.BoardModel;
import com.home.automation.homeboard.domain.CommandModel;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

public interface CommandService {

    Optional<CommandModel> createCommand(CommandModel command);

    Optional<CommandModel> findCommandById(Long commandId);

    Optional<CommandModel> deleteCommand(Long commandId);

    Optional<CommandModel> updateCommand(Long commandId, CommandModel command);

    Optional<CommandModel> addActionsToCommand(Long commandId, List<Long> actionIds);

    Optional<CommandModel> disableActionsForCommand(Long commandId, List<Long> actionIds, Boolean status);

    MultiValueMap<BoardModel, ActionModel> splitActionsPerBoardFromCommand(Long commandId);

}
