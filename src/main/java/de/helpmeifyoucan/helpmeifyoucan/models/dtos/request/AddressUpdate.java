package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;

import org.bson.conversions.Bson;

import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidCountry;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidDistrict;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidHouseNumber;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidStreet;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidZipCode;

public class AddressUpdate extends ModelUpdate {

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

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setStreet(String street) {
        this.street = street;

    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Bson toFilter() {
        return super.toFilter(this);
    }

}
