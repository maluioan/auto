package com.home.automation.homeboard.ws;

import java.util.List;

public class NonSubscribedBoardMessagePayload implements WSMessagePayload {

    private Object payload;
    private String boardName;
    private List<String> actionIds;

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public List<String> getActionIds() {
        return actionIds;
    }

    public void setActionIds(List<String> actions) {
        this.actionIds = actions;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Object getPayload() {
        return payload;
    }
}
