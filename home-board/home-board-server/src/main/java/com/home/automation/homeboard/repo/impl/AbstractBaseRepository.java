package com.home.automation.homeboard.repo.impl;


import com.home.automation.homeboard.domain.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractBaseRepository {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    protected EntityManager entityManager;

    /**
     *
     * @param baseModel
     * @return
     */
    public <BM extends BaseModel> BM saveBaseModel(BM baseModel) {
        entityManager.persist(baseModel);
        return baseModel;
    }

    /**
     *
     * @param baseModelId
     * @param nameQuery
     * @param <BM>
     * @return
     */
    protected  <BM extends BaseModel> Optional<BM> findBaseModelById(final Object baseModelId, final String nameQuery) {
        final Query findCommand = entityManager.createNamedQuery(nameQuery);
        findCommand.setParameter("id", baseModelId);
        findCommand.setParameter("active", Boolean.TRUE);

        final List<BM> resultList = findCommand.getResultList();
        if (resultList.size() > 1) {
            logger.warn(String.format("Multiple entries of type %s found with id %s."), resultList.get(0).getClass().getSimpleName(), baseModelId);
        }

        return (resultList.size() != 0)
                ? Optional.ofNullable(resultList.get(0))
                : Optional.empty();
    }

    /**
     *
     * @param baseModelId
     * @param namedQuery
     * @return
     */
    protected int removeBaseModel(final Long baseModelId, final String namedQuery) {
        final Query removeCommand = entityManager.createNamedQuery(namedQuery);
        removeCommand.setParameter("id", baseModelId);
        removeCommand.setParameter("active", Boolean.FALSE);

        int removedRecordCount = removeCommand.executeUpdate();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Marked as inactive %d commands", removedRecordCount));
        }
        return removedRecordCount;
    }

    /**
     *
     * @param parameters
     * @param updateNamedQuery
     * @return
     */
    protected int updateBaseModel(final Map<String, Object> parameters, final String updateNamedQuery) {
        final Query updateCommand = entityManager.createNamedQuery(updateNamedQuery);
        parameters.entrySet().forEach(entry -> updateCommand.setParameter(entry.getKey(), entry.getValue()));

        return updateCommand.executeUpdate();
    }
}
