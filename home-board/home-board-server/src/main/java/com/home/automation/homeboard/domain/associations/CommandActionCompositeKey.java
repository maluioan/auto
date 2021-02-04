package com.home.automation.homeboard.domain.associations;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CommandActionCompositeKey implements Serializable {

    @Column(name = "action_id")
    private Long actionId;

    @Column(name = "command_id")
    private Long commandId;

    public CommandActionCompositeKey() {}

    public CommandActionCompositeKey(Long actionId, Long commandId) {
        this.actionId = actionId;
        this.commandId = commandId;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public Long getCommandId() {
        return commandId;
    }

    public void setCommandId(Long commandId) {
        this.commandId = commandId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandActionCompositeKey that = (CommandActionCompositeKey) o;
        return Objects.equals(getActionId(), that.getActionId())  &&
                Objects.equals(getCommandId(), that.getCommandId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getActionId(), getCommandId());
    }
}
