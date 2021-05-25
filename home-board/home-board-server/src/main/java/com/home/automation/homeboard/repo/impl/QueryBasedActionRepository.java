package com.home.automation.homeboard.repo.impl;

import com.home.automation.homeboard.domain.ActionModel;
import com.home.automation.homeboard.repo.ActionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
// TODO: create a repo layer without queries (using just spring Transactional (not javax))
public class QueryBasedActionRepository extends AbstractBaseRepository implements ActionRepository {

    private static final Logger logger = LoggerFactory.getLogger(QueryBasedActionRepository.class);

    @Override
    public ActionModel saveAction(final ActionModel actionModel) {
        return super.saveBaseModel(actionModel);
    }

    @Override
    public ActionModel removeAction(Long actionId) {
        final Query updateCommand = entityManager.createNamedQuery(ActionModel.TOGGLE_ACTION_WITH_ID_ACTIVE_STATUS);
        updateCommand.setParameter("id", actionId);
        updateCommand.setParameter("active", Boolean.FALSE);

        int updatedRecordsCount = updateCommand.executeUpdate();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Marked as inactive %d actions", updatedRecordsCount));
        }
        return null;
    }

    @Override
    public Optional<ActionModel> findActionById(Long actionId) {
        return super.findBaseModelById(actionId, ActionModel.FIND_ACTIVE_ACTION_WITH_ID);
    }

    @Override
    public Optional<ActionModel> findActionByExecutorId(final String executorId) {
        return super.findBaseModelById(executorId, ActionModel.FIND_ACTIVE_ACTION_WITH_EXECUTOR_ID);
    }

    @Override
    public int updateAction(Long actionId, ActionModel actionModel) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", actionModel.getName());
        parameters.put("description", actionModel.getDescription());
        parameters.put("command", actionModel.getCommand());
        parameters.put("id", actionId);

        return super.updateBaseModel(parameters, ActionModel.UPDATE_ACTION_WITH_ID_ACTIVE_STATUS);
    }
}
