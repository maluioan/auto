package com.home.automation.homeboard.controller;

import com.home.automation.homeboard.data.BoardData;
import com.home.automation.homeboard.service.HomeBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardController {

    @Autowired
    private HomeBoardService homeBoardService;

    @GetMapping(value = "/board/{boardId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BoardData retrieveAction(@PathVariable Long boardId) {
        return null;//homeBoardService.createBoard(boardId);
    }

    @PostMapping(value = "/board", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BoardData createAction(@RequestBody BoardData boardData) {
        return homeBoardService.createBoard(boardData);
    }

    @PutMapping(value = "/board/{boardId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BoardData updateAction(@PathVariable Long boardId,
                                   @RequestBody BoardData actionData) {
        return null;//homeBoardService.updateAction(actionId, actionData);
    }

    @DeleteMapping("/board/{boardId}")
    public void deleteAction(@PathVariable Long boardId) {
//        homeBoardService.deleteAction(actionId);
    }
}
