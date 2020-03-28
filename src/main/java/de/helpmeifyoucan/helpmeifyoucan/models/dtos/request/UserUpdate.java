package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;


import org.bson.conversions.Bson;

import javax.validation.constraints.Email;


public class UserUpdate extends ModelUpdate {


    @Email
    protected String email;


    protected String password;

    private String currentPassword;


    public UserUpdate() {

    }

    public String getCurrentPassword() {
        return this.currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Bson toFilter() {
        return super.toFilter(this);
    }


}
