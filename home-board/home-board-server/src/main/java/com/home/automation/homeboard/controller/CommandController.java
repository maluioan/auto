package com.home.automation.homeboard.controller;

import com.home.automation.homeboard.data.CommandData;
import com.home.automation.homeboard.service.HomeBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: verifica fiecare requet si queriuriile de hibernate, verifica sa fie batched unde-i cazul
@RestController
public class CommandController {

    @Autowired
    private HomeBoardService homeBoardService;

    @GetMapping(value = "/command/{commandDataId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandData retrieveCommand(@PathVariable final Long commandDataId) {
        return homeBoardService.findCommandById(commandDataId);
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

    @DeleteMapping("/command/{commandId}")
    public void deleteCommand(@PathVariable Long commandId) {
        homeBoardService.deleteCommand(commandId);
    }

    @PutMapping(value = "/commandaction/{commandId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandData updateActionsToCommand(@PathVariable Long commandId, @RequestParam final List<Long> actionIds) {
        return homeBoardService.addActionsToCommand(commandId, actionIds);
    }

    // TODO: change GET, not suitable
    @GetMapping(value = "/commandaction/{commandId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandData disableActionsForCommand(@PathVariable Long commandId,
                                                @RequestParam List<Long> actionIds,
                                                @RequestParam Boolean status) {
        return homeBoardService.disableActionsForCoomand(commandId, actionIds, status);
    }
}
