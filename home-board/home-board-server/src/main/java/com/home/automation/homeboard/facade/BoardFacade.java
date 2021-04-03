package com.home.automation.homeboard.facade;

import com.home.automation.homeboard.data.BoardData;

public interface BoardFacade {

    BoardData createBoard(BoardData boardData);

    BoardData findBoardById(Long boardId);

    BoardData updateBoard(Long boardId, BoardData boardData);

    BoardData deleteBoard(Long boardId);
}
