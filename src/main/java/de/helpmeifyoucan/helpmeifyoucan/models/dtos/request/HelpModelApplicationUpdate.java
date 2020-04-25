package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;

import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;

public class HelpModelApplicationUpdate {

    private String name;
    private String lastName;
    private String telephoneNr;

    public String getName() {
        return name;
    }

    public HelpModelApplicationUpdate setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public HelpModelApplicationUpdate setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getTelephoneNr() {
        return telephoneNr;
    }

    public HelpModelApplicationUpdate setTelephoneNr(String telephoneNr) {
        this.telephoneNr = telephoneNr;
        return this;
    }

    public HelpModelApplicationUpdate copyUserInformation(UserModel user) {
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.telephoneNr = user.getPhoneNr();
        return this;
    }
}
