package com.home.automation.homeboard.domain;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "boards")
public class BoardModel extends BaseModel {

    @Column(name  = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ActionModel> actions;

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

    public Set<ActionModel> getActions() {
        return actions;
    }

    public void setActions(Set<ActionModel> actions) {
        this.actions = actions;
    }

}
