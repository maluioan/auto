package com.home.automation.dispatcher.service;

public interface DispatcherService {

    void sendMessageWithFeedback(String commandId, String userName, Object payload);
}
