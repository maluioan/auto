package com.home.automation.usersserver.domain;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "user")
public class UserModel extends BaseModel {
    @Column(name = "username", unique = true, updatable = false)
    private String userName;
    @Column(name = "active")
    private Boolean active;
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    private ContactModel contactModel;

    //  TODO: READ:  https://www.baeldung.com/jpa-many-to-many
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    private Collection<RoleModel> roleModels;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Collection<RoleModel> getRoleModels() {
        return roleModels;
    }

    public void setRoleModels(Collection<RoleModel> roleModels) {
        this.roleModels = roleModels;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public ContactModel getContactModel() {
        return contactModel;
    }

    public void setContactModel(ContactModel contactModel) {
        this.contactModel = contactModel;
    }
}
