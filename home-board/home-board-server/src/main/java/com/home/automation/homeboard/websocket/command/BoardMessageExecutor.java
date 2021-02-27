package com.home.automation.homeboard.websocket.command;

public class BoardMessageExecutor implements MessageExecutor {
    @Override
    public void execute(BaseBoardCommand boardCommand) {
        System.out.println("Executing: " + boardCommand);
        // TODO: add executor service
        boardCommand.execute();
    }
}
