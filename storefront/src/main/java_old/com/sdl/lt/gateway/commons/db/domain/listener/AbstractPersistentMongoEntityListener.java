package com.sdl.lt.gateway.commons.db.domain.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

import com.mongodb.DBObject;
import com.sdl.lt.gateway.domain.AbstractPersistentEntity;

/**
 * Listener for MongoDB persistence events that endures some referential integrity constraints
 *
 * @author bdanu
 * @since Feb 10, 2012
 */
@Component
@DependsOn({"mongoTemplate"})
@SuppressWarnings("rawtypes")
public class AbstractPersistentMongoEntityListener extends AbstractMongoEventListener<AbstractPersistentEntity> {

    @Autowired
    AbstractPersistentEntityListenerHelper abstractPersistentEntityListenerHelper;

    @Override
    public void onBeforeConvert(AbstractPersistentEntity source) {
        abstractPersistentEntityListenerHelper.setBeforeConvertFields(source);
        source.beforeConvert();
    }

    @Override
    public void onBeforeSave(AbstractPersistentEntity source, DBObject dbo) {
        // this was disabled for performance reasons
        // abstractPersistentEntityListenerHelper.verifyAndSetReferences(source);        
    }

    @Override
    public void onAfterConvert(DBObject dbo, AbstractPersistentEntity source) {
        abstractPersistentEntityListenerHelper.loadDependencies(source);
    }

    @Override
    public void onAfterSave(AbstractPersistentEntity source, DBObject dbo) {
        // TODO Auto-generated method stub
        source.afterSave();
    }
}
