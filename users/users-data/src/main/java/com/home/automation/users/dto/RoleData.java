package com.home.automation.users.dto;

import javax.persistence.*;

@Entity(name = "role")
public class RoleData extends BaseData {

    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
