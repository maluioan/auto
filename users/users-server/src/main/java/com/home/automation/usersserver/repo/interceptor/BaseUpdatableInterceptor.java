package com.home.automation.usersserver.repo.interceptor;

import com.home.automation.usersserver.domain.BaseModel;
import com.home.automation.usersserver.domain.UserModel;
import org.hibernate.*;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

// LINK: http://blog.progs.be/542/date-to-java-time
// TODO: is this session scoper or app scoped?
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
        if (base instanceof UserModel) {
            final UserModel u = (UserModel)base;
            if (Objects.isNull(u.getActive())) {
                u.setActive(Boolean.FALSE);
            }
        }
        return false;
    }

}
