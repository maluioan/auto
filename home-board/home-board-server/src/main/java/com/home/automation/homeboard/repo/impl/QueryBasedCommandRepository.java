package com.home.automation.homeboard.repo.impl;

import com.home.automation.homeboard.domain.ActionModel;
import com.home.automation.homeboard.domain.CommandModel;
import com.home.automation.homeboard.domain.associations.CommandActionModel;
import com.home.automation.homeboard.repo.ActionRepository;
import com.home.automation.homeboard.repo.CommandRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
// TODO: refactor repository layer
public class QueryBasedCommandRepository extends AbstractBaseRepository implements CommandRepository {

    @Autowired
    private ActionRepository actionRepository;

    @Override
    public CommandModel saveCommand(final CommandModel commandModel) {
        CollectionUtils.emptyIfNull(commandModel.getCommandAction()).stream()
                .map(CommandActionModel::getAction)
                .forEach(actionRepository::saveAction);
        super.saveBaseModel(commandModel);
        return commandModel;
    }

    @Override
    public Optional<CommandModel> findCommandById(final Long commandId) {
        return findBaseModelById(commandId, CommandModel.FIND_ACTIVE_COMMAND_WITH_ID);
    }

    @Override
    public void removeCommand(final Long commandId) {
        super.removeBaseModel(commandId, CommandModel.TOGGLE_COMMAND_WITH_ID_ACTIVE_STATUS);
    }

    @Override
    public int updateCommand(Long commandId, CommandModel commandModel) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", commandModel.getName());
        parameters.put("description", commandModel.getDescription());
        parameters.put("id", commandId);

        return super.updateBaseModel(parameters, CommandModel.UPDATE_COMMAND_WITH_ID_ACTIVE_STATUS);
    }

    @Override
    public List<CommandActionModel> addActionsToCommand(CommandModel command, List<Long> actionIds) {
        final Query namedQuery = entityManager.createNamedQuery(ActionModel.BATCH_FETCH_BY_ID, ActionModel.class);
        namedQuery.setParameter("actionIds", actionIds);

        // TODO: trateaza cazuriile in care o actiune nu exista, o asociere cu actiunea deja exista
        final List<ActionModel> actions = namedQuery.getResultList();

        return actions.stream()
                .map(action -> createAndPersistCommandAction(command, action))
                .collect(Collectors.toList());
    }

    @Override
    public void changeCommandActionStatus(CommandModel commandId, List<ActionModel> actionIds, boolean activeStatus) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("command", commandId);
        parameters.put("action", actionIds);
        parameters.put("active", activeStatus);

        super.updateBaseModel(parameters, CommandActionModel.TOGGLE_COMMAND_ACTION_STATUS);
    }

    private CommandActionModel createAndPersistCommandAction(CommandModel command, ActionModel action) {
        final CommandActionModel cam = new CommandActionModel(command, action);
        entityManager.persist(cam);
        return cam;
    }
}