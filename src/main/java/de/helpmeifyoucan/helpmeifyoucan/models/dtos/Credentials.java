package de.helpmeifyoucan.helpmeifyoucan.models.dtos;

import javax.validation.constraints.Email;

public class Credentials {

    @Email(message = "Please fill in Email")
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}