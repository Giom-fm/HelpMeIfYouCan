package de.helpmeifyoucan.helpmeifyoucan.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mongodb.client.model.geojson.Geometry;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.listSerializers.ListObjectIdMapping;
import org.bson.types.ObjectId;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class Coordinates extends AbstractEntity {

    @JsonSerialize(converter = ListObjectIdMapping.class)
    protected List<ObjectId> helpOffers;

    @JsonSerialize(converter = ListObjectIdMapping.class)
    protected List<ObjectId> helpRequests;

    protected double latitude;

    protected double longitude;

    @JsonIgnore
    protected Geometry location;

    @JsonIgnore
    protected int hashCode;

    public Coordinates() {
        this.helpOffers = new LinkedList<>();
        this.helpRequests = new LinkedList<>();
    }


    public <T extends AbstractHelpModel> Coordinates addHelpModel(T model) {
        if (model instanceof HelpOfferModel) {
            this.helpOffers.add(model.getId());
        } else {
            this.helpRequests.add(model.getId());
        }
        return this;
    }

    public Geometry getLocation() {
        return location;
    }

    public Coordinates setLocation(Geometry location) {
        this.location = location;
        return this;
    }

    public <T extends AbstractHelpModel> Coordinates removeHelpModel(T model) {

        if (model instanceof HelpOfferModel) {
            this.helpOffers.remove(model.getId());
        } else {
            this.helpRequests.remove(model.getId());
        }
        return this;
    }

    public boolean noOtherRefsBesideId(ObjectId id) {
        return (this.helpOffers.isEmpty() && this.helpRequests.size() == 1 && this.helpRequests.contains(id)) || (this.helpRequests.isEmpty() && this.helpOffers.size() == 1 && this.helpOffers.contains(id));
    }

    public boolean hasRefToId(Object id) {
        return this.helpRequests.contains(id) || this.helpOffers.contains(id);
    }

    public <T extends AbstractHelpModel> Coordinates clearRefsAndAddId(T helpModel) {
        this.helpRequests.clear();
        this.helpOffers.clear();

        return this.addHelpModel(helpModel);
    }

    public List<ObjectId> getHelpRequests() {
        return helpRequests;
    }

    public Coordinates setHelpRequests(List<ObjectId> requests) {
        this.helpRequests = requests;
        return this;
    }

    public List<ObjectId> getHelpOffers() {
        return helpOffers;
    }

    public Coordinates setHelpOffers(List<ObjectId> offers) {
        this.helpOffers = offers;
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
        this.location = new Point(new Position(this.longitude, this.latitude));
        return this;
    }

    public boolean noHelpModelRefs() {
        return this.helpRequests.isEmpty() && this.helpOffers.isEmpty();
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

    public String toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", this.getId());
            obj.put("location", this.location);
            obj.put("helpOffers", this.helpOffers);
            obj.put("helpRequests", this.helpRequests);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Objects.equals(this.location, that.location) &&
                getHashCode() == that.getHashCode() &&
                Objects.equals(helpOffers, that.helpOffers) &&
                Objects.equals(helpRequests, that.helpRequests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLatitude(), getLongitude());
    }

    @Override
    public String toString() {
        return "Coordinates{" + "id=" + id +
                ", helpOffers=" + helpOffers +
                ", helpRequests=" + helpRequests +
                ", hashCode=" + hashCode +
                '}';
    }
}
