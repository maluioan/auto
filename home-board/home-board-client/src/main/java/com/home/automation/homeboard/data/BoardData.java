package com.home.automation.homeboard.data;

import java.util.List;

// ex.:  https://www.baeldung.com/spring-boot-multiple-modules
// ex: https://dzone.com/articles/ddd-spring-boot-multi-module-maven-project
public class BoardData extends BaseData {
    private String name;
    private String description;
    private String externalBoardId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExternalBoardId() {
        return externalBoardId;
    }

    public void setExternalBoardId(String externalBoardId) {
        this.externalBoardId = externalBoardId;
    }
}
