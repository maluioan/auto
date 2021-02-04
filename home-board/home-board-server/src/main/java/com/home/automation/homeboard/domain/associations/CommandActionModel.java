package com.home.automation.homeboard.domain.associations;

import com.home.automation.homeboard.domain.ActionModel;
import com.home.automation.homeboard.domain.CommandModel;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "command_action")
public class CommandActionModel {

    @EmbeddedId
    private CommandActionCompositeKey compositeKey;

//    https://vladmihalcea.com/the-best-way-to-map-a-many-to-many-association-with-extra-columns-when-using-jpa-and-hibernate/
    @ManyToOne(fetch = FetchType.EAGER) // TODO: check fetch type
    @MapsId("actionId")
    @JoinColumn(name = "action_id")
    private ActionModel action;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("commandId")
    @JoinColumn(name = "command_id")
    private CommandModel command;

    @Column(name = "active")
    private Boolean enable;

    public CommandActionModel() {}

    public CommandActionModel(CommandModel command, ActionModel action) {
        this.action = action;
        this.command = command;
        this.compositeKey = new CommandActionCompositeKey();
        this.enable = true;
    }

    public CommandActionCompositeKey getCompositeKey() {
        return compositeKey;
    }

    public void setCompositeKey(CommandActionCompositeKey compositeKey) {
        this.compositeKey = compositeKey;
    }

    public ActionModel getAction() {
        return action;
    }

    public void setAction(ActionModel action) {
        this.action = action;
    }

    public CommandModel getCommand() {
        return command;
    }

    public void setCommandModel(CommandModel command) {
        this.command = command;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandActionModel that = (CommandActionModel) o;
        return getAction().equals(that.getAction()) &&
               getCommand().equals(that.getCommand());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAction(), getCommand());
    }
}
