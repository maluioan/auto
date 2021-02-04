package com.home.automation.homeboard.domain;

import org.apache.commons.collections4.SetUtils;

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
                name= CommandModel.UPDATE_COMMAND_WITh_ID_ACTIVE_STATUS,
                query = "update commands com set com.name = :name, com.description = :description where com.id = :id"
        )
})

@Entity(name = "commands")
public class CommandModel extends BaseModel {

    public static final String FIND_ACTIVE_COMMAND_WITH_ID = "CommandModel.findById";
    public static final String TOGGLE_COMMAND_WITH_ID_ACTIVE_STATUS = "CommandModel.inactivateById";
    public static final String UPDATE_COMMAND_WITh_ID_ACTIVE_STATUS = "CommandModel.updateCommand";

    //@NaturalId
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(
        mappedBy = "command",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<ActionModel> actions = SetUtils.hashSet();

    private Boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ActionModel> getActions() {
        return actions;
    }

    public void setActions(Set<ActionModel> actions) {
        this.actions = actions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void addAction(final ActionModel action) {
        this.getActions().add(action);
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
