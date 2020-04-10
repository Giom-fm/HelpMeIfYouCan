package de.helpmeifyoucan.helpmeifyoucan.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.AddressUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.listSerializers.ListObjectIdMapping;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.bson.types.ObjectId;

import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.AddressUpdate;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidCountry;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidDistrict;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidHouseNumber;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidStreet;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidZipCode;

public class AddressModel extends AbstractEntity {

    @ValidStreet
    protected String street;

    @ValidDistrict
    protected String district;

    @ValidZipCode
    protected String zipCode;

    @ValidCountry
    protected String country;

    @ValidHouseNumber
    protected String houseNumber;

    @JsonSerialize(converter = ListObjectIdMapping.class)
    protected List<ObjectId> users;

    @JsonIgnore
    protected int hashCode;


    public AddressModel() {
        this.users = new LinkedList<>();
    }

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

        List<ObjectId> users = new ArrayList<>();
        users.add(user);
        return this.setUsers(users);
    }

    public AddressModel removeUserAddress(ObjectId user) {
        this.users.remove(user);
        return this;
    }

    public AddressModel setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    public String getHouseNumber() {
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

    public boolean containsUser(ObjectId user) {
        if (this.users == null) {
            return false;
        }
        return this.users.contains(user);
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
                ", users=" + users +
                ", hashCode=" + hashCode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressModel that = (AddressModel) o;
        return getHouseNumber().equals(that.getHouseNumber()) &&
                getStreet().equals(that.getStreet()) &&
                getDistrict().equals(that.getDistrict()) &&
                getZipCode().equals(that.getZipCode()) &&
                getCountry().equals(that.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStreet(), getDistrict(), getZipCode(), getCountry(), getHouseNumber());
    }
}
