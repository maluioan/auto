package com.home.automation.homeboard.controller;

import com.home.automation.homeboard.data.BoardData;
import com.home.automation.homeboard.facade.BoardFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardController {

    @Autowired
    private BoardFacade boardFacade;

    @GetMapping(value = "/board/{boardId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BoardData retrieveAction(@PathVariable Long boardId) {
        return null;
    }

    @PostMapping(value = "/board", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BoardData createAction(@RequestBody BoardData boardData) {
        return boardFacade.createBoard(boardData);
    }

    @PutMapping(value = "/board/{boardId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BoardData updateAction(@PathVariable Long boardId,
                                   @RequestBody BoardData boardData) {
        return boardFacade.updateBoard(boardId, boardData);
    }

    @DeleteMapping("/board/{boardId}")
    public BoardData deleteAction(@PathVariable Long boardId) {
        return boardFacade.deleteBoard(boardId);
    }
}
