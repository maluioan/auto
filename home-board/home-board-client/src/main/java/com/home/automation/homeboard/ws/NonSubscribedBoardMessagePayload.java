package com.home.automation.homeboard.ws;

import java.util.List;

public class NonSubscribedBoardMessagePayload implements WSMessagePayload {

    private String boardName;

    private List<String> actions;

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }
}
