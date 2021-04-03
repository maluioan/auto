package com.home.automation.homeboard.controller;

import com.home.automation.homeboard.data.ActionData;
import com.home.automation.homeboard.facade.ActionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class ActionController {

    @Autowired
    private ActionFacade actionFacade;

    @GetMapping(value = "/action/{actionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ActionData retrieveAction(@PathVariable Long actionId) {
        return actionFacade.findActionById(actionId);
    }

    @PostMapping(value = "/action", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ActionData createAction(@RequestBody ActionData actionData) {
        return actionFacade.createAction(actionData);
    }

    @PutMapping(value = "/action/{actionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ActionData updateAction(@PathVariable Long actionId,
                                   @RequestBody ActionData actionData) {
        return actionFacade.updateAction(actionId, actionData);
    }

    @DeleteMapping("/action/{actionId}")
    public ActionData deleteAction(@PathVariable Long actionId) {
        return actionFacade.deleteAction(actionId);
    }
}
