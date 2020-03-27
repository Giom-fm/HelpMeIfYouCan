package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;



import org.bson.conversions.Bson;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


public class AddressUpdate extends ModelUpdate {


    public String street;

    public String district;

    @Min(value = 0, message = "please fill in House Number between 0 and 999")
    @Max(value = 999, message = "please fill in House Number between 0 and 999")
    public int houseNumber;

    public String zipCode;

    public String country;


    public void setHouseNumber(int houseNumber) {
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
