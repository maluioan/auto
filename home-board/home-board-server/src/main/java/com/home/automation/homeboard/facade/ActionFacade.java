package com.home.automation.homeboard.facade;

import com.home.automation.homeboard.data.ActionData;

public interface ActionFacade {

    ActionData findActionById(Long actionId);

    ActionData createAction(ActionData actionData);

    ActionData updateAction(Long actionId, ActionData actionData);

    ActionData deleteAction(final Long actionId);
}
