package com.home.automation.homeboard.domain;

import com.home.automation.homeboard.domain.associations.CommandActionModel;
import org.apache.commons.collections4.SetUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

//@NaturalIdCache
@NamedQueries({
        @NamedQuery(
                name= CommandModel.FIND_ACTIVE_COMMAND_WITH_ID,
                query = "SELECT object (com) from commands com where com.active = :active and com.id = :id"
        ),
        @NamedQuery(
                name= CommandModel.TOGGLE_COMMAND_WITH_ID_ACTIVE_STATUS,
                query = "update commands com set com.active = :active where com.id = :id"
        ),
        @NamedQuery(
                name= CommandModel.UPDATE_COMMAND_WITH_ID_ACTIVE_STATUS,
                query = "update commands com set com.name = :name, com.description = :description where com.id = :id"
        ),
        @NamedQuery(
                name= CommandModel.FIND_ALL_ACTIVE_COMMANDS,
                query = "SELECT object (com) from commands com where com.active = :active" // JPQL syntax
        )
})

@Entity(name = "commands")
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "active"})) // TODO: create in DB?
@DynamicUpdate
public class CommandModel extends BaseModel {

    public static final String FIND_ALL_ACTIVE_COMMANDS = "CommandModel.findAllActiveCommands";
    public static final String FIND_ACTIVE_COMMAND_WITH_ID = "CommandModel.findById";
    public static final String TOGGLE_COMMAND_WITH_ID_ACTIVE_STATUS = "CommandModel.inactivateById";
    public static final String UPDATE_COMMAND_WITH_ID_ACTIVE_STATUS = "CommandModel.updateBaseModel";

    //@NaturalId
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "command",
            fetch = FetchType.EAGER,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private Set<CommandActionModel> commandAction = SetUtils.hashSet();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CommandActionModel> getCommandAction() {
        return commandAction;
    }

    public void setCommandAction(Set<CommandActionModel> commandAction) {
        this.commandAction = commandAction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addAction(final ActionModel action, boolean updateAction) {
        final CommandActionModel cam = new CommandActionModel(this, action);

        this.commandAction.add(cam);
        if (updateAction) {
            action.getCommandAction().add(cam);
        }
    }

    public void removeAction(ActionModel action) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandModel that = (CommandModel) o;
        return getName().equals(that.getName()) &&
                Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription());
    }
}
