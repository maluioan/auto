package com.sdl.lt.gateway.commons.db.repository;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.sdl.lt.gateway.commons.exception.SystemException;
import com.sdl.lt.gateway.domain.Sequence;

@Repository
public class SequenceRepositoryImpl extends AbstractRepositoryImpl implements SequenceRepository {

    @Override
    public long getNext(String name) {
        Criteria queryCriteria = Criteria.where(getMongoPath(Sequence.class, "name")).is(name);
        Query query = new Query(queryCriteria);
        Update update = new Update();
        update.inc(getMongoPath(Sequence.class, "value"), 1);
        Sequence sequence = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true).upsert(true), Sequence.class);
        if (sequence == null) {
            throw new SystemException("Unknown sequence");
        }
        return sequence.getValue();
    }

}
