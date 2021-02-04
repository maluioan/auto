package com.home.automation.homeboard.converters.base;

import com.home.automation.homeboard.data.BaseData;
import com.home.automation.homeboard.domain.BaseModel;

public interface BaseConverter<M extends BaseModel, D extends BaseData> {

    D convertToData(M model);

    M convertToModel(D data);
}
