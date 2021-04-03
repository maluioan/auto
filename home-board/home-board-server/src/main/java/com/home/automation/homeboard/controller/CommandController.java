package com.home.automation.homeboard.controller;

import com.home.automation.homeboard.data.CommandData;
import com.home.automation.homeboard.facade.CommandFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: verifica fiecare requet si queriuriile de hibernate, verifica sa fie batched unde-i cazul
@RestController
public class CommandController {

    @Autowired
    private CommandFacade commandFacade;

    @GetMapping(value = "/command/{commandDataId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandData retrieveCommand(@PathVariable final Long commandDataId) {
        return commandFacade.findCommandById(commandDataId);
    }

    @PostMapping(value = "/command", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandData createCommand(@RequestBody final CommandData commandData) {
        return commandFacade.createCommand(commandData);
    }

    @PutMapping(value = "/command/{commandId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandData updateCommand(@PathVariable Long commandId,
                                     @RequestBody final CommandData commandData) {
        return commandFacade.updateCommand(commandId, commandData);
    }

    @DeleteMapping("/command/{commandId}")
    public CommandData deleteCommand(@PathVariable Long commandId) {
        return commandFacade.deleteCommand(commandId);
    }

    @PutMapping(value = "/commandaction/{commandId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandData updateActionsToCommand(@PathVariable Long commandId, @RequestParam final List<Long> actionIds) {
        return commandFacade.addActionsToCommand(commandId, actionIds);
    }

    // TODO: change GET, not suitable
    @GetMapping(value = "/commandaction/{commandId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandData disableActionsForCommand(@PathVariable Long commandId,
                                                @RequestParam List<Long> actionIds,
                                                @RequestParam Boolean status) {
        return commandFacade.disableActionsForCoomand(commandId, actionIds, status);
    }
}
