package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import org.bson.conversions.Bson;

import javax.validation.constraints.NotNull;

import static com.mongodb.client.model.Updates.set;

public class CoordinatesUpdate extends ModelUpdate {

    @NotNull
    public double latitude;
    @NotNull
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
        return set("location", new Point(new Position(this.longitude, this.latitude)));
    }
}
