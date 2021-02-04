package com.home.automation.homeboard.repo.interceptor;

import com.home.automation.homeboard.domain.BaseModel;
import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Iterator;

// TODO: gaseste un loc comun pt toti interceptori de genul asta
public interface HomeHibernateInterceptor<T extends BaseModel> extends Interceptor {

    @Override
    default boolean onLoad(Object o, Serializable serializable, Object[] objects, String[] strings, Type[] types) throws CallbackException {
        return false;
    }

    @Override
    default boolean onFlushDirty(Object o, Serializable serializable, Object[] objects, Object[] objects1, String[] strings, Type[] types) throws CallbackException {
        if (o == null) {
            throw new IllegalArgumentException("Saved entity cannot be null");
        }
        return o.getClass().isAssignableFrom(BaseModel.class) && onFlushDirtyInternal((T) o, serializable, objects, objects1, strings, types);
    }

    boolean onFlushDirtyInternal(T o, Serializable serializable, Object[] objects, Object[] objects1, String[] strings, Type[] types) throws CallbackException;

    @Override
    default boolean onSave(Object o, Serializable serializable, Object[] objects, String[] strings, Type[] types) throws CallbackException {
        if (o == null) {
            throw new IllegalArgumentException("Saved entity cannot be null");
        }
        return o.getClass().isAssignableFrom(BaseModel.class) && onSaveInternal((T) o, serializable, objects, strings, types);
    }

    boolean onSaveInternal(T o, Serializable serializable, Object[] objects, String[] strings, Type[] types);

    @Override
    default void onDelete(Object o, Serializable serializable, Object[] objects, String[] strings, Type[] types) throws CallbackException {

    }

    @Override
    default void onCollectionRecreate(Object o, Serializable serializable) throws CallbackException {

    }

    @Override
    default void onCollectionRemove(Object o, Serializable serializable) throws CallbackException {

    }

    @Override
    default void onCollectionUpdate(Object o, Serializable serializable) throws CallbackException {

    }

    @Override
    default void preFlush(Iterator iterator) throws CallbackException {

    }

    @Override
    default void postFlush(Iterator iterator) throws CallbackException {

    }

    @Override
    default Boolean isTransient(Object o) {
        return null;
    }

    @Override
    default int[] findDirty(Object o, Serializable serializable, Object[] objects, Object[] objects1, String[] strings, Type[] types) {
        return new int[0];
    }

    @Override
    default Object instantiate(String s, EntityMode entityMode, Serializable serializable) throws CallbackException {
        return null;
    }

    @Override
    default String getEntityName(Object o) throws CallbackException {
        return null;
    }

    @Override
    default Object getEntity(String s, Serializable serializable) throws CallbackException {
        return null;
    }

    @Override
    default void afterTransactionBegin(Transaction transaction) {

    }

    @Override
    default void beforeTransactionCompletion(Transaction transaction) {

    }

    @Override
    default void afterTransactionCompletion(Transaction transaction) {

    }

    @Override
    default String onPrepareStatement(String s) {
        return null;
    }
}
