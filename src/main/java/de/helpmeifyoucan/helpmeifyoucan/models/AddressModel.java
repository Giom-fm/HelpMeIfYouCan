package de.helpmeifyoucan.helpmeifyoucan.models;


import de.helpmeifyoucan.helpmeifyoucan.models.dtos.AddressUpdate;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class AddressModel extends AbstractEntity {

    @NotNull(message = "please fill in Street")
    private String street;
    @NotNull(message = "please fill in District")
    private String district;

    @NotNull(message = "please fill in ZipCode")
    private int zipCode;

    @NotNull(message = "please fill in Country")
    private String country;

    private List<ObjectId> users;

    private int hashCode;

    public List<ObjectId> getUsers() {
        return users;
    }

    public AddressModel setUsers(List<ObjectId> users) {
        this.users = users;

        return this;
    }

    public AddressModel addUserAddress(ObjectId user) {
        this.users.add(user);
        return this;
    }

    public AddressModel removeUserAddress(ObjectId user) {
        this.users.remove(user);
        return this;
    }

    public boolean noUserReferences() {
        return this.users.isEmpty();
    }

    public int getZipCode() {
        return zipCode;
    }

    public AddressModel setZipCode(int zipCode) {
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
        if (!update.getStreet().isBlank()) {
            this.street = update.getStreet();
        }
        if (!update.getDistrict().isBlank()) {
            this.street = update.getStreet();
        }
        if (!(update.getZipCode() == 0)) {
            this.zipCode = update.getZipCode();
        }
        if (!update.getCountry().isBlank()) {
            this.country = update.getCountry();
        }

        return this.calculateHash().generateId();
    }

    public int getHashCode() {
        return hashCode;
    }

    @Override
    public AddressModel generateId() {
        this.setId(new ObjectId());
        return this;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", district='" + district + '\'' +
                ", zipCode=" + zipCode +
                ", country='" + country + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressModel address = (AddressModel) o;
        return getZipCode() == address.getZipCode() &&
                getStreet().equals(address.getStreet()) &&
                getDistrict().equals(address.getDistrict()) &&
                getCountry().equals(address.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStreet(), getDistrict(), getZipCode(), getCountry());
    }
}
