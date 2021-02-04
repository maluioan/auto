package com.sdl.lt.gateway.commons.db.repository;

import com.sdl.lt.gateway.commons.exception.SystemException;
import com.sdl.lt.gateway.domain.AbstractPersistentEntity;

public abstract class UpdateWithVersioningTemplate<T> {
    public abstract void processExistingEntity(T existingEntity);

    public void mergeIfVersionConflict(T existingEntity, T newEntity) {
        throw new SystemException("Version conflict when updating " + newEntity.getClass() + " with version "
                + ((AbstractPersistentEntity) newEntity).getVersion() + " while existing version was "
                + ((AbstractPersistentEntity) existingEntity).getVersion());
    }

}
