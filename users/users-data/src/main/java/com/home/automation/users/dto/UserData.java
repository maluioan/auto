package com.home.automation.users.dto;

import java.util.Collection;

public class UserData extends BaseData {
    private String userName;
    private Boolean active;
    private String password;
    private String name;
    private String surname;
    private ContactData contactData;
    private Collection<RoleData> roleData;


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

    public Collection<RoleData> getRoleData() {
        return roleData;
    }

    public void setRoleData(Collection<RoleData> roleData) {
        this.roleData = roleData;
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

    public ContactData getContactData() {
        return contactData;
    }

    public void setContactData(ContactData contactData) {
        this.contactData = contactData;
    }
}
