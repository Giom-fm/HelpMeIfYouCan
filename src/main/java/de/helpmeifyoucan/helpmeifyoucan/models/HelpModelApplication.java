package de.helpmeifyoucan.helpmeifyoucan.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.helpmeifyoucan.helpmeifyoucan.utils.objectSerializers.ObjectIdMapping;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

public class HelpModelApplication extends AbstractEntity {

    @NotNull
    private String message;

    private String name;

    private String lastName;

    @JsonSerialize(converter = ObjectIdMapping.class)
    private ObjectId user;

    private String telephoneNr;

    @JsonSerialize(converter = ObjectIdMapping.class)
    private ObjectId modelId;

    private String helpModelType;

    private boolean read;

    protected Date created;


    public Date getCreated() {
        return created;
    }

    public HelpModelApplication setCreated(Date created) {
        this.created = created;
        return this;
    }

    public String getHelpModelType() {
        return helpModelType;
    }

    public HelpModelApplication setHelpModelType(String helpModelType) {
        this.helpModelType = helpModelType;
        return this;
    }

    public ObjectId getModelId() {
        return modelId;
    }

    public HelpModelApplication setModelId(ObjectId modelId) {
        this.modelId = modelId;
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

    public boolean isRead() {
        return read;
    }

    public HelpModelApplication setRead(boolean read) {
        this.read = read;
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
        this.setCreated(new Date());
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HelpModelApplication that = (HelpModelApplication) o;
        return isRead() == that.isRead() &&
                Objects.equals(getMessage(), that.getMessage()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getLastName(), that.getLastName()) &&
                Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getTelephoneNr(), that.getTelephoneNr()) &&
                Objects.equals(getModelId(), that.getModelId()) &&
                Objects.equals(getHelpModelType(), that.getHelpModelType()) &&
                Objects.equals(getCreated(), that.getCreated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessage(), getName(), getLastName(), getUser(), getTelephoneNr(), getModelId(), getHelpModelType(), isRead(), getCreated());
    }

    @Override
    public String toString() {
        return "HelpModelApplication{" +
                "message='" + message + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", user=" + user +
                ", telephoneNr='" + telephoneNr + '\'' +
                ", modelId=" + modelId +
                ", helpModelType='" + helpModelType + '\'' +
                ", read=" + read +
                ", created=" + created +
                '}';
    }
}
