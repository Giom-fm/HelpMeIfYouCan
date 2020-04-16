package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;

import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.*;
import org.bson.conversions.Bson;

public class AddressUpdate extends AbstractModelUpdate {

    @ValidStreet(canBeNull = true)
    public String street;
    @ValidDistrict(canBeNull = true)
    public String district;
    @ValidHouseNumber(canBeNull = true)
    public String houseNumber;
    @ValidZipCode(canBeNull = true)
    public String zipCode;
    @ValidCountry(canBeNull = true)
    public String country;

    public AddressUpdate setStreet(String street) {
        this.street = street;
        return this;
    }

    public AddressUpdate setDistrict(String district) {
        this.district = district;
        return this;
    }

    public AddressUpdate setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
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
        return super.toFilter(this);
    }


}
