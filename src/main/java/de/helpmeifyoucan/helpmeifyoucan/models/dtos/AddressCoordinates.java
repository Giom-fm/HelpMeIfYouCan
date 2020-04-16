package de.helpmeifyoucan.helpmeifyoucan.models.dtos;

public class AddressCoordinates {

    private double latitude;

    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public AddressCoordinates setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public AddressCoordinates setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }
}
