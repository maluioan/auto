package com.home.automation.homeboard.repo.impl;

import com.home.automation.homeboard.domain.BaseModel;
import com.home.automation.homeboard.domain.BoardModel;
import com.home.automation.homeboard.repo.BoardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class QueryBasedBoardRepository extends AbstractBaseRepository implements BoardRepository {

    private static final Logger logger = LoggerFactory.getLogger(QueryBasedActionRepository.class);

    @Override
    public BoardModel saveBoard(BoardModel boardModel) {
        return super.saveBaseModel(boardModel);
    }

    @Override
    public BoardModel removeBoard(BoardModel boardModel) {
        entityManager.remove(boardModel);
        return boardModel;
    }

    @Override
    public Optional<BoardModel> findBoardById(Long boardId) {
        return super.findBaseModelById(boardId, BoardModel.FIND_BOARD_WITH_ID);
    }

    @Override
    public int updateBoard(Long boardId, BoardModel board) {
        // TODO: implement
        return 0;
    }
}
