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
//    https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
    private Set<CommandActionModel> commandAction = SetUtils.hashSet();

    @ManyToOne(fetch = FetchType.LAZY)
    private BoardModel board;

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

    public BoardModel getBoard() {
        return board;
    }

    public void setBoard(BoardModel board) {
        this.board = board;
    }
}




//    Post post = new Post("First post");
//
//post.getComments().add(
//        new PostComment("My first review")
//        );
//        post.getComments().add(
//        new PostComment("My second review")
//        );
//        post.getComments().add(
//        new PostComment("My third review")
//        );
//
//        entityManager.persist(post);