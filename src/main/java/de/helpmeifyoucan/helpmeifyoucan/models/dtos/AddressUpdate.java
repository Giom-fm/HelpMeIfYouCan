package de.helpmeifyoucan.helpmeifyoucan.models.dtos;


import de.helpmeifyoucan.helpmeifyoucan.utils.Database;
import org.bson.BsonDocument;
import org.bson.Document;

public class AddressUpdate {

    private String street;

    private String district;

    private int zipCode;

    private String country;

//TODO REGEX

    public BsonDocument toDocument() {
        return new Document().toBsonDocument(AddressUpdate.class, Database.pojoCodec);
    }

    public String getStreet() {
        return street;
    }

    public String getDistrict() {
        return district;
    }

    public int getZipCode() {
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

    public AddressUpdate setZipCode(int zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public AddressUpdate setCountry(String country) {
        this.country = country;
        return this;
    }
}
