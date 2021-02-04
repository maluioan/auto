package com.home.automation.homeboard.exception;

import com.home.automation.homeboard.domain.BaseModel;

public class BaseModelNotFoundException extends RuntimeException {

    private final Long baseModelId;

    private final Class<? extends BaseModel> baseModelClass;

    public BaseModelNotFoundException(String message, Long id, Class<? extends BaseModel> clazz) {
        super(message);
        this.baseModelId = id;
        this.baseModelClass = clazz;
    }

    public Long getBaseModelId() {
        return baseModelId;
    }

    public Class<? extends BaseModel> getBaseModelClass() {
        return baseModelClass;
    }
}
