package com.home.automation.homeboard.repo;

import com.home.automation.homeboard.domain.ActionModel;

public interface ActionRepository {

    ActionModel saveOrUpdateAction(ActionModel commandModel);

    ActionModel removeCommand(ActionModel commandModel);
}
