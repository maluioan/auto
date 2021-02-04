package com.home.automation.homeboard.data;

public class ActionData extends BaseData {
    private String name;
    private String command;
    private String description;
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
}
