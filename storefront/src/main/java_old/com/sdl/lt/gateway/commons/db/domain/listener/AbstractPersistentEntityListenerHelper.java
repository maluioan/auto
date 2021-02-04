package com.sdl.lt.gateway.commons.db.domain.listener;

import com.sdl.lt.gateway.commons.db.repository.AbstractRepository;
import com.sdl.lt.gateway.commons.domain.metadata.DependencyMetadata;
import com.sdl.lt.gateway.commons.domain.metadata.DomainRegistry;
import com.sdl.lt.gateway.commons.exception.SystemException;
import com.sdl.lt.gateway.commons.metadata.DbLocalReference;
import com.sdl.lt.gateway.commons.metadata.DbRemoteReference;
import com.sdl.lt.gateway.commons.remoteservice.RemoteRestCallHandler;
import com.sdl.lt.gateway.commons.rest.GlobalServiceList;
import com.sdl.lt.gateway.commons.util.*;
import com.sdl.lt.gateway.commons.util.LocalFetchStrategy.FetchModes;
import com.sdl.lt.gateway.domain.AbstractPersistentEntity;
import com.sdl.lt.gateway.domain.ApiVersion;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Helper component for the events that are triggered on AbstractPersistentEntity operations
 *
 * @author rmoldovan
 * @since Jun 20, 2012
 */
@Component
public class AbstractPersistentEntityListenerHelper implements ApplicationContextAware {

    // required=false is to ease some unit testing
    @Autowired(required = false)
    private DomainRegistry domainRegistry;

    private ApplicationContext applicationContext;

    @Autowired
    private GlobalServiceList globalServiceList;

    // required=false is to ease some unit testing
    @Autowired(required = false)
    private RemoteRestCallHandler remoteRestCallHandler;

    @Value("${max.get.batch.size:-1}")
    private int maxGetBatchSize;

    @Autowired
    private CommonReflectionUtils reflectionUtils;

    /**
     * Called before insert/update of an entity, updates creation date or last modified date
     *
     * @param source
     */
    public void setBeforeConvertFields(AbstractPersistentEntity source) {
        if (source.getCreationDate() == null || source.getId() == null) {
            source.setCreationDate(ApplicationUtil.getGMTNowDate());
            source.setLastModifiedDate(source.getCreationDate());
            if (source.getVersion() == null) {
                source.setVersion((long) 1);
            }
            if (source.getApiVersionTransient() == null) {
                ApiVersion apiVersion = (ApiVersion) RequestLocalContext.getFromLocalContext(LocalContextKeys.API_VERSION);
                if (apiVersion == null) {
                    throw new SystemException("API version is null!");
                }
                source.setApiVersion(apiVersion);
            } else {
                source.setApiVersion(source.getApiVersionTransient());
            }
        } else {
            source.setLastModifiedDate(ApplicationUtil.getGMTNowDate());
        }
        /*String apiKeyId = RequestLocalContextUtils.getApiKeyId();
        if (apiKeyId != null) {
            source.setApiKeyId(apiKeyId);
        }*/
    }

    /**
     * Called after an entity has been loaded from repository, any dependent entities that need to be loaded via a remote call are loaded
     * into the object
     *
     * @param source
     */
    public void loadDependencies(AbstractPersistentEntity source) {
        Set<DependencyMetadata> dependencies = domainRegistry.getReferences().get(source.getClass());
        if (dependencies == null) {
            return;
        }
        for (DependencyMetadata dependencyMetadata : dependencies) {
            try {
                DbLocalReference dbLocalReference = dependencyMetadata.getDbLocalReference();
                if (dbLocalReference != null) {
                    AbstractRepository repository = (AbstractRepository) applicationContext.getBean(dbLocalReference.repository());
                    Object parentId = PropertyAccessorFactory.forBeanPropertyAccess(source).getPropertyValue("id");
                    List results = repository.getObjectsByParent(dbLocalReference.clazz(), dbLocalReference.parentField(), parentId.toString());
                    if (results != null) {
                        Field targetField = reflectionUtils.getPropertyField(source.getClass(), dependencyMetadata.getFieldName());
                        targetField.setAccessible(true);
                        Class targetClass = targetField.getType();
                        if (Collection.class.isAssignableFrom(targetClass)) {
                            ((Collection) targetField.get(source)).addAll(results);
                        } else {
                            if (!results.isEmpty()) {
                                targetField.set(source, results.get(0));
                            }
                        }
                    }
                } else {
                    DbRemoteReference dbRemoteReference = dependencyMetadata.getDbRemoteReference();
                    if (dbRemoteReference == null || !dbRemoteReference.fetch()) {
                        return;
                    }
                    Object referenceValue = PropertyAccessorFactory.forBeanPropertyAccess(source).getPropertyValue(dependencyMetadata.getFieldName());
                    Object entity = getRemoteEntity(dependencyMetadata, referenceValue);
                    PropertyAccessorFactory.forBeanPropertyAccess(source).setPropertyValue(dbRemoteReference.entityDestinationField(), entity);
                }
            } catch (Throwable e) {
                throw new SystemException(e);
            }
        }
    }

    private Object getRemoteEntity(DependencyMetadata dependencyMetadata, Object referenceValue) throws Throwable {

        FetchModes dependencyFetchModes = dependencyMetadata.getFetchMode();
        boolean isNoLinksAlreadyPresent = !LocalFetchStrategy.add(dependencyFetchModes);

        try {

            DbRemoteReference dbRemoteReference = dependencyMetadata.getDbRemoteReference();
            String serviceId = dbRemoteReference.service();
            String methodName = dbRemoteReference.method();
            ApiVersion apiVersion = (ApiVersion) RequestLocalContext.getFromLocalContext(LocalContextKeys.API_VERSION);
            Class remoteInterface = Class
                    .forName(globalServiceList.getRemoteInterfaceForServiceId(serviceId, apiVersion == null ? null : apiVersion.toString()));
            if (referenceValue instanceof String) {
                String entityId = (String) referenceValue;
                if (entityId != null) {
                    Object remoteEntityResponse = remoteRestCallHandler.invoke(null,
                            remoteInterface.getDeclaredMethod(methodName, new Class[]{String.class}), new Object[]{entityId});
                    return PropertyAccessorFactory.forBeanPropertyAccess(remoteEntityResponse).getPropertyValue(
                            dbRemoteReference.entityInResponseField());
                }
            } else if (referenceValue instanceof List) {
                Collection<String> entityIdsList = (Collection<String>) referenceValue;
                List<Object> entities = new ArrayList<Object>();
                List<String> batchIds = new ArrayList<String>();
                for (String entityId : entityIdsList) {
                    if (entityId == null) {
                        continue;
                    }
                    batchIds.add(entityId);
                    if (batchIds.size() == maxGetBatchSize) {
                        // batch limit reached make remote call
                        entities.addAll(getRemoteBatch(dbRemoteReference, methodName, remoteInterface, entities, batchIds));
                        // clean batch
                        batchIds.clear();
                    }
                }
                // send last batch
                if (!batchIds.isEmpty()) {
                    entities.addAll(getRemoteBatch(dbRemoteReference, methodName, remoteInterface, entities, batchIds));
                }
                return entities;
            }
            // should not happen
            return null;

        } finally {
            if (!isNoLinksAlreadyPresent) {
                LocalFetchStrategy.remove(dependencyFetchModes);
            }
        }
    }

    private List getRemoteBatch(DbRemoteReference dbRemoteReference, String methodName, Class remoteInterface, List<Object> entities, List<String> batchIds)
            throws Throwable, NoSuchMethodException {
        Object remoteEntityResponse = remoteRestCallHandler.invoke(null, remoteInterface.getDeclaredMethod(methodName, new Class[]{List.class}),
                new Object[]{batchIds});
        return (List) PropertyAccessorFactory.forBeanPropertyAccess(remoteEntityResponse).getPropertyValue(dbRemoteReference.entityInResponseField());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
