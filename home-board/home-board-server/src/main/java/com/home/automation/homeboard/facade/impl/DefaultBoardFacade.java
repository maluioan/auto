package com.home.automation.homeboard.facade.impl;

import com.home.automation.homeboard.data.BoardData;
import com.home.automation.homeboard.domain.BoardModel;
import com.home.automation.homeboard.facade.BoardFacade;
import com.home.automation.homeboard.service.model.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
// TODO; finish board stuff
public class DefaultBoardFacade implements BoardFacade {

    @Autowired
    private BoardService boardService;

    @Override
    public BoardData createBoard(BoardData boardData) {
        final BoardModel bm = new BoardModel();
        bm.setId(boardData.getId());
        bm.setDescription(boardData.getDescription());
        bm.setExternalBoardId(boardData.getExternalBoardId());

        boardService.createBoard(bm);
        return null;
    }

    @Override
    public BoardData findBoardById(Long boardId) {
        boardService.findBoardById(boardId);
        return null;
    }

    @Override
    public BoardData updateBoard(Long boardId, BoardData boardData) {
        boardService.updateBoard(boardId, null);
        return null;
    }

    @Override
    public BoardData deleteBoard(Long boardId) {
        boardService.deleteBoard(boardId);
        return null;
    }
}
