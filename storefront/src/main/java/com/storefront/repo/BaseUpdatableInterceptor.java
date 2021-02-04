package com.storefront.repo;

import com.home.automation.users.dto.BaseData;
import org.hibernate.CallbackException;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

// LINK: http://blog.progs.be/542/date-to-java-time
// TODO: is this session scoper or app scoped?
@Component
public class BaseUpdatableInterceptor implements HomeHibernateInterceptor<BaseData> {

    @Override
    public boolean onFlushDirtyInternal(BaseData baseData, Serializable serializable, Object[] objects, Object[] objects1, String[] strings, Type[] types) throws CallbackException {
        baseData.setDateModified(LocalDateTime.now());
        return false;
    }

    @Override
    public boolean onSaveInternal(BaseData baseData, Serializable serializable, Object[] objects, String[] strings, Type[] types) throws CallbackException {
        baseData.setDateCreated(LocalDateTime.now());
        baseData.setDateModified(LocalDateTime.now());
        return false;
    }

}
