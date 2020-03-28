package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;


import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.Name;

public class Register extends Credentials {

    @Name
    private String name;
    @Name
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
}