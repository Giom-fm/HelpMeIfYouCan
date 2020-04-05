package de.helpmeifyoucan.helpmeifyoucan.models;

import org.bson.types.ObjectId;

import java.util.Objects;

public class HelpModelApplication extends AbstractEntity {


    private String message;

    private UserModel user;

    private String telephoneNr;

    public UserModel getUser() {
        return user;
    }

    public HelpModelApplication setUser(UserModel user) {
        this.user = user;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public HelpModelApplication setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getTelephoneNr() {
        return telephoneNr;
    }

    public HelpModelApplication setTelephoneNr(String telephoneNr) {
        this.telephoneNr = telephoneNr;
        return this;
    }

    public HelpModelApplication generateId() {
        this.id = new ObjectId();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HelpModelApplication that = (HelpModelApplication) o;
        return getUser().equals(that.getUser()) &&
                getTelephoneNr().equals(that.getTelephoneNr());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getTelephoneNr());
    }

    @Override
    public String toString() {
        return "HelpModelApplication{" +
                "message='" + message + '\'' +
                ", user=" + user +
                ", telephoneNr='" + telephoneNr + '\'' +
                ", id=" + id +
                '}';
    }
}
