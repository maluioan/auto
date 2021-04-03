package com.home.automation.homeboard.service.model;

import com.home.automation.homeboard.domain.BoardModel;

import java.util.Optional;

public interface BoardService {

    Optional<BoardModel> createBoard(BoardModel board);

    Optional<BoardModel> findBoardById(Long boardId);

    Optional<BoardModel> updateBoard(Long boardId, BoardModel board);

    Optional<BoardModel> deleteBoard(Long boardId);

}
