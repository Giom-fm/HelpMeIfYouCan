package de.helpmeifyoucan.helpmeifyoucan.models.dtos;


import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class AddressUpdate {


    private String street;

    private String district;

    private String zipCode;

    private String country;

//TODO REGEX

    public AddressUpdate() {
        this.street = "";
        this.district = "";
        this.zipCode = "";
        this.country = "";
    }

    public String getStreet() {
        return street;
    }

    public String getDistrict() {
        return district;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCountry() {
        return country;
    }

    public AddressUpdate setStreet(String street) {
        this.street = street;
        return this;
    }

    public AddressUpdate setDistrict(String district) {
        this.district = district;
        return this;
    }

    public AddressUpdate setZipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public AddressUpdate setCountry(String country) {
        this.country = country;
        return this;
    }

    public Bson toFilter() {
        List<Bson> filter = new ArrayList<>();
        if (!this.street.isEmpty()) {
            filter.add(set("street", this.street));
        }
        if (!this.district.isEmpty()) {
            filter.add(set("district", this.district));
        }
        if (!this.zipCode.isEmpty()) {
            filter.add(set("zipCode", this.zipCode));
        }
        if (!this.country.isEmpty()) {
            filter.add(set("country", this.country));
        }
        return combine(filter);

    }
}
