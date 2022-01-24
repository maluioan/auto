package com.home.automation.usersserver.startup;

public class AppStartupException extends RuntimeException {

    private Class<? extends AbstractAppStartupStrategy> startupClass;

    public AppStartupException(final Exception ex,
                               final Class<? extends AbstractAppStartupStrategy> aClass) {
        super(ex);
        this.startupClass = aClass;
    }

    public Class<? extends AbstractAppStartupStrategy> getStartupClass() {
        return startupClass;
    }
}
