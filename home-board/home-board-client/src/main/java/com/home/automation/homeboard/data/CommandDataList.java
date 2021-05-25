package com.home.automation.homeboard.data;

import java.util.List;

public class CommandDataList {

    private List<CommandData> commandDataList;
    private int requestedCount;

//    public CommandDataList() {}

//    public CommandDataList(List<CommandData> commandDataList, int requestedCount) {
//        this.commandDataList = commandDataList;
//        this.requestedCount = requestedCount;
//    }

    public List<CommandData> getCommandDataList() {
        return commandDataList;
    }

    public void setCommandDataList(List<CommandData> commandDataList) {
        this.commandDataList = commandDataList;
    }

    public int getRequestedCount() {
        return requestedCount;
    }

    public void setRequestedCount(int requestedCount) {
        this.requestedCount = requestedCount;
    }
}
