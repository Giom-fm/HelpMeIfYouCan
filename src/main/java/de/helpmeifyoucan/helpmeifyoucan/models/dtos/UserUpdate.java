package de.helpmeifyoucan.helpmeifyoucan.models.dtos;

import org.bson.Document;

public class UserUpdate {


    private String password = "";

    private String email = "";


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

    public Document toDocument() {

        var document = new Document();

        if (!this.email.equals(null)) {
            document.put("email", this.email);

        }

        //TODO password hash
        if (!this.password.equals(null)) {
            document.put("password", this.password);
        }
        return document;
    }


}
