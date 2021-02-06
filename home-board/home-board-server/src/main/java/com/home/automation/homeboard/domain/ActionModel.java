package com.home.automation.homeboard.domain;

import com.home.automation.homeboard.domain.associations.CommandActionModel;
import org.apache.commons.collections4.SetUtils;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import javax.persistence.*;
import java.util.Set;

@NamedQueries({
        @NamedQuery(
                name = ActionModel.FIND_ACTIVE_ACTION_WITH_ID,
                query = "select object (act) from actions as act where act.id = :id and act.active = :active"
        ),
        @NamedQuery(
                name = ActionModel.BATCH_FETCH_BY_ID,
                query = "select object (act) from actions as act where act.id in (:actionIds)"
        ),
        @NamedQuery(
                name = ActionModel.TOGGLE_ACTION_WITH_ID_ACTIVE_STATUS,
                query = "update actions act set act.active = :active where act.id = :id"
        ),
        @NamedQuery(
                name = ActionModel.UPDATE_ACTION_WITH_ID_ACTIVE_STATUS,
                query = "update actions act set act.name = :name, act.description = :description, act.command = :command where act.id = :id"
        )
})
@NaturalIdCache
@Entity(name = "actions")
@DynamicUpdate
public class ActionModel extends BaseModel {

    public static final String BATCH_FETCH_BY_ID = "ActionModel.fetchByIds";
    public static final String TOGGLE_ACTION_WITH_ID_ACTIVE_STATUS = "ActionModel.toggleActiveStatus";
    public static final String FIND_ACTIVE_ACTION_WITH_ID = "ActionModel.findById";
    public static final String UPDATE_ACTION_WITH_ID_ACTIVE_STATUS = "ActionModel.updateAction";

    @NaturalId
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "command")
    private String command;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "action",
            fetch = FetchType.LAZY,
            cascade = CascadeType.DETACH, // TODO: cand se salveaza ActionModel, sa nu se salveze restul
            orphanRemoval = true
    )
    private Set<CommandActionModel> commandAction = SetUtils.hashSet();

//    @ManyToOne // TODO: amanare
//    private BoardModel boardModel;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Set<CommandActionModel> getCommandAction() {
        return commandAction;
    }

    public void setCommandAction(Set<CommandActionModel> commandAction) {
        this.commandAction = commandAction;
    }

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

}
