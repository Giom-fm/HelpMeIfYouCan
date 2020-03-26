package de.helpmeifyoucan.helpmeifyoucan.models.dtos;

import org.bson.conversions.Bson;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class UserUpdate {


    private String password;

    @Email
    private String email;

    public UserUpdate() {
        this.password = "";
        this.email = "";
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }


    public UserUpdate setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserUpdate setEmail(String email) {
        this.email = email;
        return this;
    }

    public Bson toFilter() {

        List<Bson> filter = new ArrayList<>();
        if (!this.email.isEmpty()) {
            filter.add(set("email", this.email));
        }

        //TODO password hash
        if (!this.password.isEmpty()) {
            filter.add(set("password", this.password));
        }
        return combine(filter);
    }


}
