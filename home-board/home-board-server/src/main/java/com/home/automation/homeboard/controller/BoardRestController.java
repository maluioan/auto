package com.home.automation.homeboard.controller;

import com.home.automation.homeboard.data.BoardData;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class BoardRestController {

    @GetMapping(value = "/api/board", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<BoardData> getBoards() {
        return Arrays.asList(createBoardData(13l, "Board name s3cret"), createBoardData(54l, "numele meu"));
    }

    @GetMapping(value = "/api/board/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BoardData getBoardDataById(@PathVariable("id") Long boardId) {
        return createBoardData(boardId, "Board name s3cret");
    }

    private BoardData createBoardData(Long boardId, String boardName) {
        final BoardData boardData = new BoardData();
        boardData.setId(boardId);
        boardData.setName(boardName);
        return boardData;
    }

}
