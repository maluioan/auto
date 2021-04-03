package com.home.automation.homeboard.service.model.impl;

import com.home.automation.homeboard.domain.BoardModel;
import com.home.automation.homeboard.domain.CommandModel;
import com.home.automation.homeboard.exception.BaseModelNotFoundException;
import com.home.automation.homeboard.repo.BoardRepository;
import com.home.automation.homeboard.service.model.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class DefaultBoardService implements BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Override
    public Optional<BoardModel> createBoard(final BoardModel board) {
        return Optional.of(boardRepository.saveBoard(board));
    }

    @Override
    public Optional<BoardModel> findBoardById(Long boardId) {
        return Optional.of(checkAndGetBoard(boardId));
    }

    @Override
    public Optional<BoardModel> updateBoard(Long boardId, BoardModel board) {
        int updatedRows = boardRepository.updateBoard(boardId, board);
        return (updatedRows > 0) ? Optional.of(checkAndGetBoard(boardId)) : Optional.empty();
    }

    @Override
    public Optional<BoardModel> deleteBoard(Long boardId) {
        return Optional.empty();
    }

    private BoardModel checkAndGetBoard(final Long boardId) {
        final Optional<BoardModel> boardOptional = boardRepository.findBoardById(boardId);
        boardOptional.orElseThrow(() -> createBaseModelNotFoundException(String.format("Command with id %s, doesn't exist.", boardId), boardId));
        return boardOptional.get();
    }

    private BaseModelNotFoundException createBaseModelNotFoundException(final String message, final Long commandId) {
        return new BaseModelNotFoundException(message, commandId, CommandModel.class);
    }

}
