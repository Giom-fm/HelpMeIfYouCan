package de.helpmeifyoucan.helpmeifyoucan.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Objects;

public class Coordinates extends AbstractEntity {
    @JsonIgnore
    protected List<ObjectId> offers;

    @JsonIgnore
    protected List<ObjectId> requests;

    protected double latitude;

    protected double longitude;

    @JsonIgnore
    protected int hashCode;


    public List<ObjectId> getHelpOffers() {
        return offers;
    }

    public Coordinates setHelpOffers(List<ObjectId> offers) {
        this.offers = offers;
        return this;
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
        return (this.requests != null && this.requests.size() == 1 && this.requests.contains(id)) || (this.offers != null && this.offers.size() == 1 && this.offers.contains(id));
    }

    public boolean hasRefToId(Object id) {
        return (this.requests != null && this.requests.contains(id)) || (this.offers != null && this.offers.contains(id));
    }

    public List<ObjectId> getHelpRequests() {
        return requests;
    }

    public Coordinates setHelpRequests(List<ObjectId> requests) {
        this.requests = requests;
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
        return (this.requests != null && this.requests.size() > 0) || (this.offers != null && this.offers.size() > 0);
    }

    public int getHashCode() {
        return hashCode;
    }

    public Coordinates setHashCode(int hashCode) {
        this.hashCode = hashCode;
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
}
