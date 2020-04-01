package de.helpmeifyoucan.helpmeifyoucan.models;

import de.helpmeifyoucan.helpmeifyoucan.utils.PostStatusEnum;
import org.bson.types.ObjectId;

import java.util.Objects;

public abstract class AbstractHelpModel<T extends AbstractHelpModel> extends AbstractEntity {


    protected ObjectId user;

    protected Coordinates coordinates;

    protected String description;

    protected PostStatusEnum status;


    public ObjectId getUser() {
        return user;
    }

    public AbstractHelpModel<T> setUser(ObjectId user) {
        this.user = user;
        return this;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public AbstractHelpModel<T> setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AbstractHelpModel<T> setDescription(String description) {
        this.description = description;
        return this;
    }

    public PostStatusEnum getStatus() {
        return status;
    }

    public AbstractHelpModel<T> setStatus(PostStatusEnum status) {
        this.status = status;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractHelpModel<?> that = (AbstractHelpModel<?>) o;
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
        return "AbstractHelp{" +
                "user=" + user +
                ", coordinates=" + coordinates +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
