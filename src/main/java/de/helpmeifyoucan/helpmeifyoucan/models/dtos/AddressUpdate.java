package de.helpmeifyoucan.helpmeifyoucan.models.dtos;


import org.bson.Document;

public class AddressUpdate {

    private String street;

    private String district;

    private String zipCode;

    private String country;

//TODO REGEX

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

    public Document toDocument() {
        var document = new Document();
        if (!this.street.isEmpty()) {
            document.put("street", this.street);
        }
        if (!this.district.isEmpty()) {
            document.put("district", this.district);
        }
        if (!this.zipCode.isEmpty()) {
            document.put("zipCode", this.zipCode);
        }
        if (!this.country.isEmpty()) {
            document.put("country", this.country);
        }
        return document;

    }
}
