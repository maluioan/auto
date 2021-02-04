package com.home.automation.homeboard.converters;

import com.home.automation.homeboard.converters.base.AbstractBaseConverter;
import com.home.automation.homeboard.data.BoardData;
import com.home.automation.homeboard.domain.BaseModel;
import com.home.automation.homeboard.domain.BoardModel;
import org.springframework.stereotype.Component;

@Component
public class BoardConverter extends AbstractBaseConverter<BaseModel, BoardData> {

    public BoardConverter() {
        super(BaseModel.class, BoardData.class);
    }
}
