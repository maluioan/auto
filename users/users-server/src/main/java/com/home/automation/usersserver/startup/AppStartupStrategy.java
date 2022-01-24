package com.home.automation.usersserver.startup;

public interface AppStartupStrategy {

    void execute() throws AppStartupException;
}
