package com.home.automation.homeboard.converters;

import com.home.automation.homeboard.converters.base.AbstractBaseConverter;
import com.home.automation.homeboard.data.ActionData;
import com.home.automation.homeboard.domain.ActionModel;
import org.springframework.stereotype.Component;

@Component
public class ActionConverter extends AbstractBaseConverter<ActionModel, ActionData> {

    public ActionConverter() {
        super(ActionModel.class, ActionData.class);
    }
}
