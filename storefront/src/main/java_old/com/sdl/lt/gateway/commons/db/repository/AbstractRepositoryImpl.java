package com.sdl.lt.gateway.commons.db.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.sdl.lt.gateway.commons.exception.EntityNotFoundException;
import com.sdl.lt.gateway.commons.exception.SystemException;
import com.sdl.lt.gateway.commons.rest.security.utils.PermissionsApplyAspect;
import com.sdl.lt.gateway.commons.util.CommonReflectionUtils;
import com.sdl.lt.gateway.commons.util.LocalContextKeys;
import com.sdl.lt.gateway.commons.util.RequestLocalContext;
import com.sdl.lt.gateway.domain.AbstractPersistentEntity;
import com.sdl.lt.gateway.domain.AbstractPersistentMongoEntity;
import com.sdl.lt.gateway.domain.GenericEntity;
import org.apache.commons.beanutils.PropertyUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author alex
 * @since Feb 1, 2012
 */
public abstract class AbstractRepositoryImpl implements AbstractRepository, ApplicationContextAware {

    private static final String VR = "vr";

    private static final String ID = "_id";

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractRepositoryImpl.class);

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    protected MongoTemplate primaryOnlyMongoTemplate;

    @Autowired
    protected CommonReflectionUtils refUtils;

    @Value("${mongo.numberOfReplicas}")
    private int mongoNumberOfReplicas;

    ApplicationContext applicationContext;

    @Autowired
    PermissionsApplyAspect permissionsApplyAspect;

    @PostConstruct
    public void post() {
        LOGGER.info("primaryOnlyMongoTemplate = " + primaryOnlyMongoTemplate);
    }

    @Override
    public <T> T create(T entity) {
        String serviceToken = (String) RequestLocalContext.getFromLocalContext(LocalContextKeys.SERVICE_TOKEN);
        if (serviceToken == null) {
            ((AbstractPersistentMongoEntity) entity).updateWithDefaultValueNotInsertableFields();
        }
        permissionsApplyAspect.applyPermissions(entity);
        mongoTemplate.insert(entity);
        return entity;
    }

    @Override
    public <T> Collection<T> create(Collection<T> entities, Class entityType) {
        for (T entity : entities) {
            ((AbstractPersistentEntity) entity).updateWithDefaultValueNotInsertableFields();
            permissionsApplyAspect.applyPermissions(entity);
        }
        mongoTemplate.insert(entities, entityType);
        return entities;
    }

    private <T> void updateCurrentEntityCache(T entity) {
        GenericEntity currentEntity = (GenericEntity) RequestLocalContext.getFromLocalContext(LocalContextKeys.CURRENT_ENTITY);
        if (currentEntity != null && entity.getClass().isAssignableFrom(currentEntity.getClass())
                && currentEntity.getId().toString().equals(currentEntity.getId().toString())) {
            RequestLocalContext.putInLocalContext(LocalContextKeys.CURRENT_ENTITY, entity);
        }
    }

    @Override
    public <T> void update(T entity) {
        mongoTemplate.save(entity);
        updateCurrentEntityCache(entity);
    }

    @Override
    public <T> T updateWithVersioning(Class<T> clazz, String id, UpdateWithVersioningTemplate<T> template) throws EntityNotFoundException {
        boolean eventsWerePublishedAlready = false;
        T entity = getUnchachedObjectByIdFromPrimary(clazz, id, Boolean.FALSE);
        Long existingVersion = ((AbstractPersistentEntity) entity).getVersion();
        template.processExistingEntity(entity);

        String collectionName = clazz.getAnnotation(Document.class).collection();
        BasicDBObject queryJson = new BasicDBObject();
        queryJson.append(ID, ObjectId.massageToObjectId(id));
        if (existingVersion != null) {
            queryJson.append(VR, existingVersion);
        } else {
            existingVersion = 0L;
        }

        long sleepTime = 20;
        while (true) {
            ((AbstractPersistentEntity) entity).setVersion(existingVersion + 1);
            BasicDBObject objectJson = new BasicDBObject();
            if (!eventsWerePublishedAlready) {
                applicationContext.publishEvent(new BeforeConvertEvent<T>(entity));
            }
            mongoTemplate.getConverter().write(entity, objectJson);
            if (!eventsWerePublishedAlready) {
                applicationContext.publishEvent(new BeforeSaveEvent<T>(entity, objectJson));
            }
            // issues with this have been found if you try to process multiple files at the same time for one project -> some files got lost
            // during the merge
            // use this scenario to reproduce issues related to WriteConcern here
            // the custom WriteConcern should force replica sync even if the mongo xml configuration has a SAFE or lower writeconcern
            // TODO create WriteConcern with more replicas in case replica set is higher than 2
            WriteResult updateResult = null;
            if (mongoNumberOfReplicas == 1) {
                updateResult = primaryOnlyMongoTemplate.getDb().getCollection(collectionName).update(queryJson, objectJson, false, false/*
                 * ,
                 * WriteConcern
                 * .
                 * REPLICAS_SAFE
                 */);
            } else {
                updateResult = primaryOnlyMongoTemplate.getDb().getCollection(collectionName).update(queryJson, objectJson, false, false, WriteConcern.REPLICAS_SAFE);
            }
            if ((Boolean) updateResult.getField("updatedExisting")) {
                updateCurrentEntityCache(entity);
                applicationContext.publishEvent(new AfterSaveEvent<T>(entity, objectJson));
                return entity;
            } else {
                eventsWerePublishedAlready = true;
                try {
                    Thread.sleep(sleepTime);
                    if (sleepTime < 1200) {
                        sleepTime = sleepTime * 3;
                    }
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e.getCause());
                }
                T updatedEntity = getUnchachedObjectByIdFromPrimary(clazz, id, Boolean.FALSE);
                template.mergeIfVersionConflict(updatedEntity, entity);
                existingVersion = ((AbstractPersistentEntity) updatedEntity).getVersion();
                queryJson.put(VR, existingVersion);
            }
        }
    }

    private <T> T updatePartial(List<String> fieldsList, T entity) throws EntityNotFoundException {
        String id = ((AbstractPersistentEntity) entity).getId().toString();
        T oldEntity = (T) getObjectById(entity.getClass(), id);
        applicationContext.publishEvent(new BeforeConvertEvent<T>(entity));
        BasicDBObject objectJson = new BasicDBObject();
        applicationContext.publishEvent(new BeforeSaveEvent<T>(oldEntity, objectJson));
        Update update = new Update();
        try {
            update.set(getMongoPath(entity.getClass(), "lastModifiedDate"), ((AbstractPersistentEntity) entity).getLastModifiedDate());
        } catch (Exception e) {
            throw new SystemException("Error while reading " + "lastModifiedDate" + " property for " + entity.getClass().getSimpleName() + " with id " + id, e);
        }
        for (String field : fieldsList) {
            try {
                update.set(getMongoPath(entity.getClass(), field), PropertyUtils.getSimpleProperty(entity, field));
            } catch (Exception e) {
                throw new SystemException("Error while reading " + field + " property for " + entity.getClass().getSimpleName() + " with id " + id, e);
            }
        }
        Criteria criteria = Criteria.where(ID).is(ObjectId.massageToObjectId(id));
        mongoTemplate.updateFirst(new Query(criteria), update, entity.getClass());
        T newEntity = (T) getUnchachedObjectById(entity.getClass(), id, false);
        updateCurrentEntityCache(entity);
        applicationContext.publishEvent(new AfterSaveEvent<T>(newEntity, objectJson));
        return newEntity;
    }

    @Override
    public <T> T updatePartial(T entity) throws EntityNotFoundException {
        List<String> fieldsList = refUtils.getUpdatableProperties(entity.getClass());
        return updatePartial(fieldsList, entity);

    }

    @Override
    public <T> T getObjectById(Class<T> clazz, Object id) throws EntityNotFoundException {
        return getObjectById(clazz, id, Boolean.FALSE);
    }

    /**
     * @param clazz
     * @param id
     * @param deleted if this is null, we ignore the deleted flag, otherwise we check if the entity has the provided flag
     * @return
     * @throws EntityNotFoundException
     */
    public <T> T getObjectById(Class<T> clazz, Object id, Boolean deleted) throws EntityNotFoundException {
        try {
            // check if we just got the entity when we authorised access
            GenericEntity currentEntity = (GenericEntity) RequestLocalContext.getFromLocalContext(LocalContextKeys.CURRENT_ENTITY);
            if (currentEntity != null && clazz.isAssignableFrom(currentEntity.getClass())
                    && currentEntity.getId().toString().equals(currentEntity.getId().toString())) {
                return (T) currentEntity;
            }

            T entity = getUnchachedObjectById(clazz, id, deleted);
            if (entity == null) {
                throw new EntityNotFoundException(clazz.getSimpleName() + " with id " + id + " not found");
            }
            return entity;
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Invalid entity id " + id.toString(), e);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemException("Unknown error while retrieving entity", e);
        }
    }

    @Override
    public <T> List<T> getObjectsByIds(Class<T> clazz, List ids) {
        return getObjectsByIdsWithDeletedField(clazz, ids, Boolean.FALSE);
    }

    /**
     * from must start from 1
     *
     * @param clazz
     * @param query
     * @param from
     * @param to
     * @param <T>
     * @return
     */
    @Override
    public <T> List<T> getByRange(Class<T> clazz, Query query, int from, int to) {

        return getByRangeWithDeletedField(clazz, query, from, to, Boolean.FALSE);

    }

    @Override
    public <T> List<T> getObjectsByParent(Class<T> clazz, String parentField, String parentId) throws EntityNotFoundException {
        Criteria criteria = Criteria.where(parentField).is(parentId);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, clazz);
    }

    protected <T> long count(Class<T> clazz) {
        return mongoTemplate.count(new Query(), clazz);
    }

    protected <T> long count(Class<T> clazz, String mongoClass) {
        return countWithDeletedField(clazz, mongoClass, Boolean.FALSE);
    }

    @Override
    public <T> void logicalDelete(Class<T> clazz, String id) throws EntityNotFoundException {
        AbstractPersistentEntity entity = (AbstractPersistentEntity) getObjectById(clazz, id);
        entity.setDeleted(true);
        mongoTemplate.save(entity);
    }

    @Override
    public <T> void physicalDelete(Class<T> clazz, String id) throws EntityNotFoundException {
        AbstractPersistentEntity entity = (AbstractPersistentEntity) getObjectById(clazz, id);
        mongoTemplate.remove(entity);
    }

    @Override
    public <T> void physicalDelete(Class<T> clazz, String id, Boolean deleted) throws EntityNotFoundException {
        AbstractPersistentEntity entity = (AbstractPersistentEntity) getObjectById(clazz, id, deleted);
        mongoTemplate.remove(entity);
    }

    @Override
    public <T> void physicalDelete(T t) throws EntityNotFoundException {
        mongoTemplate.remove(t);
    }

    private <T> long countWithDeletedField(Class<T> clazz, String mongoClass, Boolean deleted) {
        Criteria criteria = Criteria.where("_class").is(mongoClass);
        criteria.and("d").is(deleted);
        return mongoTemplate.count(new Query(criteria), clazz);
    }

    private <T> List<T> getByRangeWithDeletedField(Class<T> clazz, Query query, int from, int to, Boolean deleted) {
        if (from != -1 && to != -1) {
            query.skip(from - 1);
            query.limit(to - (from - 1));
        }
        // todo add deleted condition in query
        return mongoTemplate.find(query, clazz);
    }

    /*
     * DO NOT CACHE! used by update with versioning
     */
    @Override
    public <T> T getUnchachedObjectById(Class<T> clazz, Object id, Boolean deleted) throws EntityNotFoundException {
        return getUnchachedObjectByIdInternal(clazz, id, deleted, false);
    }

    @Override
    public <T> T getUnchachedObjectByIdFromPrimary(Class<T> clazz, Object id, Boolean deleted) throws EntityNotFoundException {
        return getUnchachedObjectByIdInternal(clazz, id, deleted, true);
    }

    private <T> T getUnchachedObjectByIdInternal(Class<T> clazz, Object id, Boolean deleted, boolean onlyPrimary) throws EntityNotFoundException {
        Criteria criteria = Criteria.where(ID).is(id);
        if (deleted != null) {
            criteria.and("d").is(deleted);
        }
        Query query = new Query(criteria);

        T entity = null;
        if (onlyPrimary) {
            entity = primaryOnlyMongoTemplate.findOne(query, clazz);
        } else {
            entity = mongoTemplate.findOne(query, clazz);
            if (entity == null && primaryOnlyMongoTemplate != null) {
                entity = primaryOnlyMongoTemplate.findOne(query, clazz);
            }
        }

        if (entity != null) {
            return entity;
        }

        throw new EntityNotFoundException(clazz.getSimpleName() + " with id " + id + " not found");
    }

    public <T> List<T> getObjectsByIdsWithDeletedField(Class<T> clazz, List ids, Boolean deleted) {
        Criteria criteria = Criteria.where(ID).in(ids);
        criteria.and("d").is(deleted);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, clazz);
    }

    @Override
    public <T> void physicalDeleteByAge(Class<T> clazz, String mongoClass, int timeDifferenceInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -1 * timeDifferenceInMinutes);
        Criteria criteria = Criteria.where("_class").is(mongoClass);
        criteria.and("cd").lt(calendar.getTime());
        Query query = new Query(criteria);
        LOGGER.info("physicalDeleteByAge for objects of type {} deleting {} objects", clazz, mongoTemplate.count(query, clazz));
        mongoTemplate.remove(query, clazz);
    }

    @Override
    public <T> void physicalDeleteByAgeAndConditions(Class<T> clazz, String mongoClass, int timeDifferenceInMinutes, Map<String, Object> conditions) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -1 * timeDifferenceInMinutes);
        Criteria criteria = Criteria.where("_class").is(mongoClass);
        criteria.and("cd").lt(calendar.getTime());
        for (String key : conditions.keySet()) {
            criteria.and(key).is(conditions.get(key));
        }
        Query query = new Query(criteria);
        LOGGER.info("physicalDeleteByAge for objects of type {} deleting {} objects", clazz, mongoTemplate.count(query, clazz));
        mongoTemplate.remove(query, clazz);
    }

    public String getMongoPath(Class clazz, String propertyPath) {
        return refUtils.getMongoPath(clazz, propertyPath);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public CommonReflectionUtils getRefUtils() {
        return refUtils;
    }

    public void setRefUtils(CommonReflectionUtils refUtils) {
        this.refUtils = refUtils;
    }

    @Override
    public <T> T getObjectByQuery(Class<T> clazz, Query query) {
        T result = mongoTemplate.findOne(query, clazz);
        if (result == null && primaryOnlyMongoTemplate != null) {
            result = primaryOnlyMongoTemplate.findOne(query, clazz);
        }
        return result;
    }
}
