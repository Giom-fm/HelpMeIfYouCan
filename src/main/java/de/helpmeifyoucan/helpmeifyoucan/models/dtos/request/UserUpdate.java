package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;


import org.bson.conversions.Bson;

import javax.validation.constraints.Email;


public class UserUpdate extends ModelUpdate {


    @Email
    protected String email;

    protected String password;

    private String currentPassword;

    protected String name;

    protected String lastName;

    protected String phoneNr;


    public UserUpdate() {

    }

    public String getCurrentPassword() {
        return this.currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
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
