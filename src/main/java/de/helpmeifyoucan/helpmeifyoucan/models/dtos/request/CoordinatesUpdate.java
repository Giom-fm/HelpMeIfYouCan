package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;

import org.bson.conversions.Bson;

public class CoordinatesUpdate extends ModelUpdate {

    public double latitude;

    public double longitude;

    public double getLatitude() {
        return latitude;
    }

    public CoordinatesUpdate setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public CoordinatesUpdate setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Bson toFilter() {
        return super.toFilter(this);
    }
}
