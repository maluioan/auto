package com.sdl.lt.gateway.commons.db.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;

import com.sdl.lt.gateway.commons.exception.EntityNotFoundException;

/**
 * BaseData interface for all specific repository interfaces
 *
 * @author bdanu
 */
public interface AbstractRepository {

    <T> T create(T entity);

    <T> Collection<T> create(Collection<T> entities, Class entityClass);

    <T> void update(T entity);

    <T> T updatePartial(T entity) throws EntityNotFoundException;

    <T> T updateWithVersioning(Class<T> clazz, String id, UpdateWithVersioningTemplate<T> template) throws EntityNotFoundException;

    <T> T getObjectById(Class<T> clazz, Object id) throws EntityNotFoundException;

    <T> T getObjectByQuery(Class<T> clazz, Query query);

    <T> List<T> getObjectsByParent(Class<T> clazz, String parentField, String parentId) throws EntityNotFoundException;

    <T> List<T> getObjectsByIds(Class<T> clazz, List ids);

    <T> void logicalDelete(Class<T> clazz, String id) throws EntityNotFoundException;

    <T> void physicalDelete(Class<T> clazz, String id) throws EntityNotFoundException;

    <T> void physicalDelete(Class<T> clazz, String id, Boolean deleted) throws EntityNotFoundException;

    <T> void physicalDelete(T t) throws EntityNotFoundException;

    <T> List<T> getByRange(Class<T> clazz, Query query, int from, int to);

    // <T> List<T> getByRange(Class<T> clazz, int from, int to);
    <T> void physicalDeleteByAge(Class<T> clazz, String mongoClass, int timeDifferenceInMinutes);

    <T> void physicalDeleteByAgeAndConditions(Class<T> clazz, String mongoClass, int timeDifferenceInMinutes, Map<String, Object> conditions);

    public <T> T getUnchachedObjectById(Class<T> clazz, Object id, Boolean deleted) throws EntityNotFoundException;

    public <T> T getUnchachedObjectByIdFromPrimary(Class<T> clazz, Object id, Boolean deleted) throws EntityNotFoundException;
}
