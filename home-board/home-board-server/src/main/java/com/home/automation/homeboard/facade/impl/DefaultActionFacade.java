package com.home.automation.homeboard.facade.impl;

import com.home.automation.homeboard.converters.ActionConverter;
import com.home.automation.homeboard.data.ActionData;
import com.home.automation.homeboard.domain.ActionModel;
import com.home.automation.homeboard.facade.ActionFacade;
import com.home.automation.homeboard.service.model.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultActionFacade implements ActionFacade {

    @Autowired
    private ActionConverter actionConverter;

    @Autowired
    private ActionService actionService;

    @Override
    public ActionData findActionById(Long actionId) {
        return actionService.findActionById(actionId).map(actionConverter::convertToData).get();
    }

    @Override
    public ActionData createAction(ActionData actionData) {
        final ActionModel actionModel = actionConverter.convertToModel(actionData);
        return actionService.createAction(actionModel).map(actionConverter::convertToData).get();
    }

    @Override
    public ActionData updateAction(Long actionId, ActionData actionData) {
        final ActionModel actionModel = actionConverter.convertToModel(actionData);
        return actionService.updateAction(actionId, actionModel).map(actionConverter::convertToData).get();
    }

    @Override
    public ActionData deleteAction(Long actionId) {
        return actionService.deleteAction(actionId).map(actionConverter::convertToData).get();
    }
}
