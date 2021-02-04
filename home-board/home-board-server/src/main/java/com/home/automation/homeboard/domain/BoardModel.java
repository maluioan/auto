package com.home.automation.homeboard.domain;

import javax.persistence.*;

@Entity(name = "boards")
public class BoardModel extends BaseModel {

    @Column(name  = "name")
    private String name;

    @Column(name = "description")
    private String description;

    //    @OneToMany(mappedBy = "boardModel")
//    private Collection<CommandActionModel> boardModelAssociations;

//    @OneToMany(mappedBy = "boardModel")
//    private Collection<ActionModel> actions;

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

    //    public Collection<CommandActionModel> getBoardModelAssociations() {
//        return boardModelAssociations;
//    }
//
//    public void setBoardModelAssociations(Collection<CommandActionModel> boardModelAssociations) {
//        this.boardModelAssociations = boardModelAssociations;
//    }

//    public Collection<ActionModel> getActions() {
//        return actions;
//    }
//
//    public void setActions(Collection<ActionModel> actions) {
//        this.actions = actions;
//    }

}
