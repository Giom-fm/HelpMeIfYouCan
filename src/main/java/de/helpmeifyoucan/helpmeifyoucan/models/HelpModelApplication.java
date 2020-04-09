package de.helpmeifyoucan.helpmeifyoucan.models;

import org.bson.types.ObjectId;

import java.util.Objects;

public class HelpModelApplication extends AbstractEntity {

    private String message;

    private String name;

    private String lastName;

    private ObjectId user;

    private String telephoneNr;

    private ObjectId requestId;

    public ObjectId getRequestId() {
        return requestId;
    }

    public HelpModelApplication setRequestId(ObjectId requestId) {
        this.requestId = requestId;
        return this;
    }

    public HelpModelApplication addUserDetails(UserModel user) {
        this.lastName = user.getLastName();
        this.name = user.getName();
        this.telephoneNr = user.getPhoneNr();
        this.user = user.getId();
        return this;
    }

    public String getName() {
        return name;
    }

    public HelpModelApplication setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public HelpModelApplication setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public ObjectId getUser() {
        return user;
    }

    public HelpModelApplication setUser(ObjectId user) {
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
        this.setId(new ObjectId());
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
