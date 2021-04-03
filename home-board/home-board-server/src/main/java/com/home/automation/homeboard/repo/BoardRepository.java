package com.home.automation.homeboard.repo;

import com.home.automation.homeboard.domain.BoardModel;

import java.util.Optional;

public interface BoardRepository {

    BoardModel saveBoard(BoardModel boardModel);

    BoardModel removeBoard(BoardModel boardModel);

    Optional<BoardModel> findBoardById(Long boardId);

    int updateBoard(Long boardId, BoardModel board);
}
