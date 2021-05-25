package com.storefront.data;

public class FEActionData {
    private String id;
    private String name;
    private String command;
    private String parentCommandName;
    private String actionType;
    private String executorId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getParentCommandName() {
        return parentCommandName;
    }

    public void setParentCommandName(String parentCommandName) {
        this.parentCommandName = parentCommandName;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public String getExecutorId() {
        return executorId;
    }
}
