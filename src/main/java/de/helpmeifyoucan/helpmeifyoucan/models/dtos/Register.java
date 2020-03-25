package de.helpmeifyoucan.helpmeifyoucan.models.dtos;

import javax.validation.constraints.NotNull;

public class Register extends Credentials {

    @NotNull
    private String phoneNr;
    @NotNull
    private String name;
    @NotNull
    private String lastName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getphoneNr() {
        return phoneNr;
    }

    public void setphoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

}