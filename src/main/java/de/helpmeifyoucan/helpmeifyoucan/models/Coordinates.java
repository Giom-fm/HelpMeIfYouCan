package de.helpmeifyoucan.helpmeifyoucan.models;

import org.bson.types.ObjectId;

import java.util.List;
import java.util.Objects;

public class Coordinates extends AbstractEntity {

    protected List<ObjectId> offers;

    protected List<ObjectId> requests;

    protected double latitude;

    protected double longitude;

    protected int hashCode;


    public List<ObjectId> getHelpOffers() {
        return offers;
    }

    public Coordinates setHelpOffers(List<ObjectId> offers) {
        this.offers = offers;
        return this;
    }

    public Coordinates addHelpOffer(ObjectId offer) {
        this.offers.add(offer);
        return this;
    }

    public Coordinates deleteHelpOffer(ObjectId offer) {
        this.offers.remove(offer);
        return this;
    }

    public Coordinates addHelpRequest(ObjectId request) {
        this.requests.add(request);
        return this;
    }

    public Coordinates deleteHelpRequest(ObjectId request) {
        this.requests.remove(request);
        return this;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Double.compare(that.getLatitude(), getLatitude()) == 0 &&
                Double.compare(that.getLongitude(), getLongitude()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLatitude(), getLongitude());
    }
}
