package com.home.automation.homeboard.repo.interceptor;

import com.home.automation.homeboard.domain.BaseModel;
import org.hibernate.CallbackException;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

// LINK: http://blog.progs.be/542/date-to-java-time
// TODO: is this session scoper or app scoped?
// TODO: this is not called, it has no effect
@Component
public class BaseUpdatableInterceptor implements HomeHibernateInterceptor<BaseModel> {

    @Override
    public boolean onFlushDirtyInternal(BaseModel model, Serializable serializable, Object[] objects, Object[] objects1, String[] strings, Type[] types) throws CallbackException {
        if (model instanceof BaseModel) {
            final BaseModel base = (BaseModel)model;
            base.setDateModified(LocalDateTime.now());
        }
        return false;
    }

    @Override
    public boolean onSaveInternal(BaseModel base, Serializable serializable, Object[] objects, String[] strings, Type[] types) throws CallbackException {
        base.setDateCreated(LocalDateTime.now());
        base.setDateModified(LocalDateTime.now());
        return false;
    }

}
