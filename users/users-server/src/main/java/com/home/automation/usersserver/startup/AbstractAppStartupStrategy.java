package com.home.automation.usersserver.startup;

import com.home.automation.usersserver.mockdata.MockUserCreationStartupStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAppStartupStrategy implements AppStartupStrategy {

    private static final Logger logger = LoggerFactory.getLogger(MockUserCreationStartupStrategy.class);

    @Override
    public void execute() throws AppStartupException {
        logger.info("** Executing startup strategy: " + this.getClass().getSimpleName());
        try {
            this.executeInternal();
        } catch (Exception ex) {
            logger.debug(String.format("** Error executing startup strategy: " + this.getClass().getSimpleName()));
            throw new AppStartupException(ex, this.getClass());
        }
        logger.info("** Done executing startup strategy: " + this.getClass().getSimpleName());
    }

    protected abstract void executeInternal() throws Exception;

}
