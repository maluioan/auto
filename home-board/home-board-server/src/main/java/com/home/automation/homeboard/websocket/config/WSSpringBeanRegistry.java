package com.home.automation.homeboard.websocket.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WSSpringBeanRegistry {

    private static final Logger logger = LoggerFactory.getLogger(WSSpringBeanRegistry.class);

    private static WSSpringBeanRegistry INSTANCE = new WSSpringBeanRegistry();

    @Autowired
    private ApplicationContext applicationContext;

    public static <T> Optional<T> getBeanByTypeAndName(Class<T> clazz, final String beanName) {
        T object = null;
        try {
            object = INSTANCE.applicationContext.getBean(beanName, clazz);
        } catch (BeansException be) {
            logger.warn(be.getMessage());
        }
        return Optional.ofNullable(object);
    }

    private WSSpringBeanRegistry() {}
}
