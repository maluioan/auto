package com.home.automation.homeboard.data;

public class ActionData extends BaseData {
    private String name;
    private String command;
    private String description;
    private String room;
    private String parentCommandName;
    private String actionType;
    private String executorId;

    private BoardData board;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public BoardData getBoard() {
        return board;
    }

    public void setBoard(BoardData board) {
        this.board = board;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getParentCommandName() {
        return parentCommandName;
    }

    public void setParentCommandName(String parentCommandName) {
        this.parentCommandName = parentCommandName;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }
}
