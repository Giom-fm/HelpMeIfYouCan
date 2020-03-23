package de.helpmeifyoucan.helpmeifyoucan.models;


import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Address extends AbstractEntity {

    @NotNull(message = "please fill in Street")
    private String street;
    @NotNull(message = "please fill in District")
    private String district;

    @NotNull(message = "please fill in ZipCode")
    private int zipCode;

    @NotNull(message = "please fill in Country")
    private String country;


    private int hashCode;

    public int getZipCode() {
        return zipCode;
    }

    public Address setZipCode(int zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public Address setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public String getDistrict() {
        return district;
    }

    public Address setDistrict(String district) {
        this.district = district;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public Address setStreet(String street) {
        this.street = street;
        return this;
    }

    public Address calculateHash() {
        this.hashCode = hashCode();
        return this;
    }

    public Address setHashCode(int hashCode) {
        this.hashCode = hashCode;
        return this;
    }

    public int getHashCode() {
        return hashCode;
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
        Address address = (Address) o;
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
