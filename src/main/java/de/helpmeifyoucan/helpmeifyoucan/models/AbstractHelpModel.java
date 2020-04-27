package de.helpmeifyoucan.helpmeifyoucan.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.helpmeifyoucan.helpmeifyoucan.utils.PostStatusEnum;
import de.helpmeifyoucan.helpmeifyoucan.utils.listSerializers.ListApplicationSerializer;
import de.helpmeifyoucan.helpmeifyoucan.utils.objectSerializers.ObjectIdMapping;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractHelpModel extends AbstractEntity {


    @JsonSerialize(converter = ObjectIdMapping.class)
    protected ObjectId user;

    protected String userName;

    @JsonSerialize(using = ListApplicationSerializer.class)
    protected List<HelpModelApplication> applications;

    @NotNull
    protected Coordinates coordinates;

    @NotNull
    protected String description;

    protected PostStatusEnum status;

    protected Date datePublished;


    public AbstractHelpModel() {
        this.applications = new LinkedList<>();
    }

    public String getUserName() {
        return this.userName;
    }

    public abstract AbstractHelpModel setUserName(String userName);

    public abstract AbstractHelpModel generateId();

    public ObjectId getUser() {
        return user;
    }

    public abstract AbstractHelpModel setUser(ObjectId userId);

    public abstract HelpModelApplication acceptApplication(ObjectId application);

    public boolean userHasApplied(ObjectId user) {
        return this.applications.stream().map(HelpModelApplication::getUser).anyMatch(x -> x.equals(user));
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public abstract <T extends AbstractHelpModel> T setCoordinates(Coordinates coordinates);

    public String getDescription() {
        return description;
    }

    public PostStatusEnum getStatus() {
        return status;
    }

    public abstract AbstractHelpModel setStatus(PostStatusEnum status);

    public Date getDatePublished() {
        return datePublished;
    }

    public List<HelpModelApplication> getApplications() {
        return applications;
    }

    public abstract List<HelpModelApplication> combineApplications();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractHelpModel that = (AbstractHelpModel) o;
        return getCoordinates().equals(that.getCoordinates()) &&
                getDescription().equals(that.getDescription()) &&
                getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCoordinates(), getDescription(), getStatus());
    }

    @Override
    public String toString() {
        return "AbstractHelpModel{" +
                "user=" + user +
                ", applications=" + applications +
                ", coordinates=" + coordinates +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", datePublished=" + datePublished +
                ", id=" + id +
                '}';
    }
}
