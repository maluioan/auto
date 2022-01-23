package com.home.automation.dispatcher.wsclient.frame.handlers;

import com.home.automation.dispatcher.wsclient.messages.BoardStompMessage;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class CompositeBoardFrameHandler implements BoardFrameHandler {

    private List<BoardFrameHandler> boardFrameHandlers;

    @Override
    public boolean canHandleFrame(BoardStompMessage stompMessage) {
        return true;
    }

    @Override
    public void handleFrame(final BoardStompMessage stompMessage) {
        // TODO: add functionality that cna be changed at runtime
        CollectionUtils.emptyIfNull(getBoardFrameHandlers()).stream()
                .filter(obs -> obs.canHandleFrame(stompMessage))
                .forEach(obs -> obs.handleFrame(stompMessage));
    }

    public List<BoardFrameHandler> getBoardFrameHandlers() {
        return boardFrameHandlers;
    }

    public void setBoardFrameHandlers(List<BoardFrameHandler> boardFrameHandlers) {
        this.boardFrameHandlers = boardFrameHandlers;
    }
}
