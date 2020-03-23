package de.helpmeifyoucan.helpmeifyoucan.models.dtos;

public class Login {

    private String name;
    private String lastName;
    private String token;

    public Login(String name, String lastName, String token) {
        this.name = name;
        this.lastName = lastName;
        this.token = token;
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}