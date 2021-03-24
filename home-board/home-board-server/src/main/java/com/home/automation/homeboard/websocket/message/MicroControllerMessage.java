package com.home.automation.homeboard.websocket.message;

import com.home.automation.homeboard.websocket.message.request.BoardRequestMessage;


public class MicroControllerMessage extends BoardRequestMessage
{
    private String boardId;
    private String type;

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
