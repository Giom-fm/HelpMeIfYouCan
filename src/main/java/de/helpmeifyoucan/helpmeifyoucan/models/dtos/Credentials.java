package de.helpmeifyoucan.helpmeifyoucan.models.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class Credentials {

    @Email(message = "Please fill in Email")
    private String email;
    @NotNull
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