package de.helpmeifyoucan.helpmeifyoucan.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.EmbeddedCoordsMapper;
import org.bson.types.ObjectId;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


@JsonSerialize(using = EmbeddedCoordsMapper.class)
public class Coordinates extends AbstractEntity {

    @JsonIgnore
    protected List<ObjectId> offers;

    @JsonIgnore
    protected List<ObjectId> requests;

    protected double latitude;

    protected double longitude;

    @JsonIgnore
    protected int hashCode;

    public Coordinates() {
        this.offers = new LinkedList<>();
        this.requests = new LinkedList<>();
    }


    public <T extends AbstractHelpModel> Coordinates addHelpModel(T model) {
        if (model instanceof HelpOfferModel) {
            this.offers.add(model.getId());
        } else {
            this.requests.add(model.getId());
        }
        return this;
    }

    public <T extends AbstractHelpModel> Coordinates removeHelpModel(T model) {

        if (model instanceof HelpOfferModel) {
            this.offers.remove(model.getId());
        } else {
            this.requests.remove(model.getId());
        }
        return this;
    }

    public boolean noOtherRefsBesideId(ObjectId id) {
        return (this.offers.isEmpty() && this.requests.size() == 1 && this.requests.contains(id)) || (this.requests.isEmpty() && this.offers.size() == 1 && this.offers.contains(id));
    }

    public boolean hasRefToId(Object id) {
        return this.requests.contains(id) || this.offers.contains(id);
    }

    public <T extends AbstractHelpModel> Coordinates clearRefsAndAddId(T helpModel) {
        this.requests.clear();
        this.offers.clear();

        return this.addHelpModel(helpModel);
    }

    public List<ObjectId> getHelpRequests() {
        return requests;
    }

    public Coordinates setHelpRequests(List<ObjectId> requests) {
        this.requests = requests;
        return this;
    }

    public List<ObjectId> getHelpOffers() {
        return offers;
    }

    public Coordinates setHelpOffers(List<ObjectId> offers) {
        this.offers = offers;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Coordinates setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Coordinates setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Coordinates calculateHashCode() {
        this.hashCode = this.hashCode();
        return this;
    }

    public boolean noHelpModelRefs() {
        return this.requests.isEmpty() && this.offers.isEmpty();
    }

    public int getHashCode() {
        return hashCode;
    }

    public Coordinates setHashCode(int hashCode) {
        this.hashCode = hashCode;
        return this;
    }

    public Coordinates generateId() {
        super.setId(new ObjectId());
        return this;
    }

    public Coordinates mergeWithUpdate(CoordinatesUpdate update) {

        super.mergeWithUpdate(update, this);
        return this.calculateHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Double.compare(that.getLatitude(), getLatitude()) == 0 &&
                Double.compare(that.getLongitude(), getLongitude()) == 0 &&
                getHashCode() == that.getHashCode() &&
                Objects.equals(offers, that.offers) &&
                Objects.equals(requests, that.requests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLatitude(), getLongitude());
    }

    @Override
    public String toString() {
        return "Coordinates{" + "id=" + id +
                ", helpOffers=" + offers +
                ", helpRequests=" + requests +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", hashCode=" + hashCode +
                '}';
    }
}
