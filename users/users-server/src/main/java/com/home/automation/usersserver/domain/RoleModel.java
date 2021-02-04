package com.home.automation.usersserver.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "role")
public class RoleModel extends BaseModel {

    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
