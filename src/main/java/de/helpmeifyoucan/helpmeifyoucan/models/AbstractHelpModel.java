package de.helpmeifyoucan.helpmeifyoucan.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.helpmeifyoucan.helpmeifyoucan.utils.ObjectIdMapping;
import de.helpmeifyoucan.helpmeifyoucan.utils.PostStatusEnum;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public abstract class AbstractHelpModel extends AbstractEntity {


    @JsonSerialize(converter = ObjectIdMapping.class)
    protected ObjectId user;

    protected List<HelpModelApplication> applications;

    protected Coordinates coordinates;

    protected String description;

    protected PostStatusEnum status;

    protected Date datePublished;


    public ObjectId getUser() {
        return user;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public String getDescription() {
        return description;
    }

    public PostStatusEnum getStatus() {
        return status;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public List<HelpModelApplication> getApplications() {
        return applications;
    }


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
