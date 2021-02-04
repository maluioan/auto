package com.home.automation.usersserver.converters;

import com.home.automation.users.dto.BaseData;
import com.home.automation.usersserver.domain.BaseModel;

public interface BaseConverter<M extends BaseModel, D extends BaseData> {

    D convertToData(M model);

    M convertToModel(D data);
}
