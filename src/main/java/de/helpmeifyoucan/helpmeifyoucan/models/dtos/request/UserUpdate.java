package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;


import org.bson.conversions.Bson;

import javax.validation.constraints.Email;

public class UserUpdate extends ModelUpdate {

    @Email
    protected String email;

    protected String password;


    public UserUpdate setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserUpdate setEmail(String email) {
        this.email = email;
        return this;
    }


    public Bson toFilter() {
        return super.toFilter(this);
    }

}
