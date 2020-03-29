package de.helpmeifyoucan.helpmeifyoucan.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.AddressUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.ListObjectIdMapping;
import org.bson.types.ObjectId;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AddressModel extends AbstractEntity {

    @NotNull(message = "please fill in Street")
    protected String street;
    @NotNull(message = "please fill in District")
    protected String district;

    @NotNull(message = "please fill in ZipCode")
    protected String zipCode;

    @NotNull(message = "please fill in Country")
    protected String country;

    @Min(value = 0, message = "please fill in House Number between 0 and 999")
    @Max(value = 999, message = "please fill in House Number between 0 and 999")
    protected int houseNumber;

    @JsonSerialize(converter = ListObjectIdMapping.class)
    protected List<ObjectId> users;

    @JsonIgnore
    protected int hashCode;

    public List<ObjectId> getUsers() {
        return this.users;
    }

    public AddressModel setUsers(List<ObjectId> users) {
        this.users = users;
        return this;
    }

    public AddressModel addUserAddress(ObjectId user) {
        if (this.users != null) {
            this.users.add(user);
            return this;
        }

        return this.setUsers(Collections.singletonList(user));
    }

    public AddressModel removeUserAddress(ObjectId user) {
        this.users.remove(user);
        return this;
    }

    public AddressModel setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    public int getHouseNumber() {
        return this.houseNumber;
    }

    public boolean noUserReferences() {
        return this.users.isEmpty();
    }

    public String getZipCode() {
        return zipCode;
    }

    public AddressModel setZipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public AddressModel setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public String getDistrict() {
        return district;
    }

    public AddressModel setDistrict(String district) {
        this.district = district;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public AddressModel setStreet(String street) {
        this.street = street;
        return this;
    }

    public AddressModel calculateHash() {
        this.hashCode = hashCode();
        return this;
    }

    public AddressModel setHashCode(int hashCode) {
        this.hashCode = hashCode;
        return this;
    }

    public AddressModel mergeWithAddressUpdate(AddressUpdate update) {
        super.mergeWithUpdate(update, this);
        return this.calculateHash();
    }

    public int getHashCode() {
        return hashCode;
    }

    public AddressModel generateId() {
        this.setId(new ObjectId());
        return this;
    }

    @Override
    public String toString() {
        return "AddressModel{" +
                "id='" + super.getId() +
                ", street='" + street + '\'' +
                ", district='" + district + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", country='" + country + '\'' +
                ", houseNumber=" + houseNumber +
                ", hashCode=" + hashCode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressModel that = (AddressModel) o;
        return getHouseNumber() == that.getHouseNumber() &&
                getStreet().equals(that.getStreet()) &&
                getDistrict().equals(that.getDistrict()) &&
                getZipCode().equals(that.getZipCode()) &&
                getCountry().equals(that.getCountry()) &&
                getUsers().equals(that.getUsers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStreet(), getDistrict(), getZipCode(), getCountry(), getHouseNumber());
    }
}
