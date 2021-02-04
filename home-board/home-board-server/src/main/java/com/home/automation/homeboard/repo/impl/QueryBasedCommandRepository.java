package com.home.automation.homeboard.repo.impl;

import com.home.automation.homeboard.domain.ActionModel;
import com.home.automation.homeboard.domain.CommandModel;
import com.home.automation.homeboard.domain.associations.CommandActionModel;
import com.home.automation.homeboard.repo.ActionRepository;
import com.home.automation.homeboard.repo.CommandRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
// TODO: refactor repository layer
public class QueryBasedCommandRepository implements CommandRepository {

    private static final Logger logger = LoggerFactory.getLogger(QueryBasedCommandRepository.class);

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    private ActionRepository actionRepository;

    @Override
    public CommandModel saveCommand(final CommandModel commandModel) {
        CollectionUtils.emptyIfNull(commandModel.getActions()).stream()
                .map(CommandActionModel::getAction)
                .forEach(actionRepository::saveOrUpdateAction);
        entityManager.persist(commandModel);
        return commandModel;
    }

    @Override
    public Optional<CommandModel> findCommandById(final Long commandId) {
        final Query findCommand = entityManager.createNamedQuery(CommandModel.FIND_ACTIVE_COMMAND_WITH_ID);
        findCommand.setParameter("id", commandId);
        findCommand.setParameter("active", Boolean.TRUE);

        final List<CommandModel> resultList = findCommand.getResultList();
        if (resultList.size() > 1) {
            logger.warn("Multiple commands found with id: " + commandId);
        }

        return (resultList.size() != 0)
                ? Optional.ofNullable(resultList.get(0))
                : Optional.empty();
    }

    @Override
    public void removeCommand(final Long commandId) {
        final Query updateCommand = entityManager.createNamedQuery(CommandModel.TOGGLE_COMMAND_WITH_ID_ACTIVE_STATUS);
        updateCommand.setParameter("id", commandId);
        updateCommand.setParameter("active", Boolean.FALSE);

        int updatedRecordsCount = updateCommand.executeUpdate();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Marked as inactive %d commands", updatedRecordsCount));
        }
    }

    @Override
    public int updateCommand(Long commandId, CommandModel commandModel) {
        final Query updateCommand = entityManager.createNamedQuery(CommandModel.UPDATE_COMMAND_WITh_ID_ACTIVE_STATUS);
        updateCommand.setParameter("name", commandModel.getName());
        updateCommand.setParameter("description", commandModel.getDescription());
        updateCommand.setParameter("id", commandId);

        return updateCommand.executeUpdate();
    }

    @Override
    public Optional<CommandModel> addActionsToCommand(CommandModel commandModel, List<ActionModel> actions) {
        return null;
    }
}