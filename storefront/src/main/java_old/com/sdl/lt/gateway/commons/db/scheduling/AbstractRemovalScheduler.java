package com.sdl.lt.gateway.commons.db.scheduling;

import java.util.Map;

import com.sdl.lt.gateway.commons.db.repository.AbstractRepository;

/**
 * @author rmoldovan
 * @date Sep 7, 2012
 */
public class AbstractRemovalScheduler {

    public void removeEntitiesByAge(AbstractRepository repository, Class clazz, String mongoClass, int minimumAgeInMinutes) {
        repository.physicalDeleteByAge(clazz, mongoClass, minimumAgeInMinutes);
    }

    public void removeEntitiesByAgeAndConditions(AbstractRepository repository, Class clazz, String mongoClass, int minimumAgeInMinutes,
                                                 Map<String, Object> conditions) {
        repository.physicalDeleteByAgeAndConditions(clazz, mongoClass, minimumAgeInMinutes, conditions);
    }
}
