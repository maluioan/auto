package com.home.automation.homeboard.data;

import java.util.Collection;

public class CommandData extends BaseData {
    private String name;
    private String description;
    private Collection<ActionData> actions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<ActionData> getActions() {
        return actions;
    }

    public void setActions(Collection<ActionData> actions) {
        this.actions = actions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
