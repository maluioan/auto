package com.home.automation.homeboard.domain.associations;

import com.home.automation.homeboard.domain.ActionModel;
import com.home.automation.homeboard.domain.CommandModel;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "command_action")
@NamedQueries(
        @NamedQuery(
                name = CommandActionModel.TOGGLE_COMMAND_ACTION_STATUS,
                query = "update command_action ca set ca.active = :active where ca.command = :command and ca.action in (:action)"
        )
)
public class CommandActionModel {

    public static final String TOGGLE_COMMAND_ACTION_STATUS = "CommandActionModel.toggle_active";

    @EmbeddedId
    private CommandActionCompositeKey compositeKey;

//    https://vladmihalcea.com/the-best-way-to-map-a-many-to-many-association-with-extra-columns-when-using-jpa-and-hibernate/
    @ManyToOne(fetch = FetchType.LAZY) // TODO: check fetch type
    @MapsId("actionId")
    @JoinColumn(name = "action_id")
    private ActionModel action;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("commandId")
    @JoinColumn(name = "command_id")
    private CommandModel command;

    private Boolean active = Boolean.TRUE;

    public CommandActionModel() {}

    public CommandActionModel(CommandModel command, ActionModel action) {
        this.action = action;
        this.command = command;
        this.compositeKey = new CommandActionCompositeKey();
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandActionModel that = (CommandActionModel) o;
        return getAction().equals(that.getAction()) &&
               getCommand().equals(that.getCommand()) &&
               Objects.equals(getActive(), that.getActive());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAction(), getCommand(), getActive());
    }
}
