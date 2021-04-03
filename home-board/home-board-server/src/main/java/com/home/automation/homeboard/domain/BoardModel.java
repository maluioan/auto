package com.home.automation.homeboard.domain;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "boards")
public class BoardModel extends BaseModel {

    public static final String FIND_BOARD_WITH_ID = "BoardModel.findById";

    @Column(name  = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name  = "externalBoardId")
    private String externalBoardId;

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

    public String getExternalBoardId() {
        return externalBoardId;
    }

    public void setExternalBoardId(String externalBoardId) {
        this.externalBoardId = externalBoardId;
    }
}
