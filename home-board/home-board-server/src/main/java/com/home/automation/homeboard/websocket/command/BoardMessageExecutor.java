package com.home.automation.homeboard.websocket.command;

public class BoardMessageExecutor implements MessageExecutor {
    @Override
    public void execute(final BaseBoardCommand boardCommand) {
        // TODO: add executor service
        boardCommand.execute();
    }
}
