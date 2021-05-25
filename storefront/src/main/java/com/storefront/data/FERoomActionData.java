package com.storefront.data;

import java.util.Collection;

public class FERoomActionData {

    private String room;

    private Collection<FEActionData> actions;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Collection<FEActionData> getActions() {
        return actions;
    }

    public void setActions(Collection<FEActionData> actions) {
        this.actions = actions;
    }
}
