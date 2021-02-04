package com.home.automation.homeboard.controller;

import com.home.automation.homeboard.data.ActionData;
import com.home.automation.homeboard.data.BoardData;
import com.home.automation.homeboard.data.CommandData;
import com.home.automation.homeboard.service.HomeBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommandExecutionController {

    @Autowired
    private HomeBoardService homeBoardService;

    @GetMapping(value = "/command/{commandDataId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandData retrieveCommand(@PathVariable final Long commandDataId) {
        return homeBoardService.getCommandDataById(commandDataId);
    }

    @PostMapping(value = "/command", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandData createCommand(@RequestBody final CommandData commandData) {
        return homeBoardService.createCommand(commandData);
    }

    @PutMapping(value = "/command/{commandId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandData updateCommand(@PathVariable Long commandId,
                                     @RequestBody final CommandData commandData) {
        return homeBoardService.updateCommand(commandId, commandData);
    }

    @DeleteMapping(value = "/command/{commandId}")
    public void deleteCommand(@PathVariable Long commandId) {
        homeBoardService.deleteCommand(commandId);
    }

    @PutMapping(value = "/command/{commandId}/actions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandData updateActionsToCommand(@PathVariable Long commandId, @RequestBody final List<Long> actionIds) {
        return homeBoardService.addActionsToCommand(commandId, actionIds);
    }

    @PostMapping(value = "/board", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BoardData createBoard(@RequestBody final BoardData boardData) {
//        return homeBoardService.addBoard(boardData);
        return null;
    }
    //
    @PutMapping(value = "/board/{boardId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BoardData updateBoard(@PathVariable Long boardId,
                                 @RequestBody final BoardData boardData) {
//        return homeBoardService.updateBoard(boardId, boardData);
        return null;
    }

}
