package com.home.automation.homeboard.domain;

import com.home.automation.homeboard.domain.associations.CommandActionModel;
import org.apache.commons.collections4.SetUtils;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import javax.persistence.*;
import java.util.Set;

@NaturalIdCache
@Entity(name = "actions")
public class ActionModel extends BaseModel {

    @NaturalId
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "command")
    private String command;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "action",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
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
