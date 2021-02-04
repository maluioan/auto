package com.home.automation.usersserver.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity(name = "contact")
public class ContactModel extends BaseModel {
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "telephone")
    private String telephone;

    // TODO: READ: https://www.baeldung.com/jpa-one-to-one
    @OneToOne(mappedBy = "contactModel")
    private UserModel userModel;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
