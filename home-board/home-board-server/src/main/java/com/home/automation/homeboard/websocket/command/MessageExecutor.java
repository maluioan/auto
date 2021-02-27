package com.home.automation.homeboard.websocket.command;

public interface MessageExecutor {

    void execute(BaseBoardCommand boardCommand);
}
