package de.helpmeifyoucan.helpmeifyoucan.models;

import java.util.List;

public class User extends AbstractEntity {

    public static final String collectionName = "CollectionName";
    private String name;
    private String lastname;
    private List<Address> addresses;
    private int phoneNr;
    private String payPal;

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public User setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public User setPhoneNr(int phoneNr) {
        this.phoneNr = phoneNr;
        return this;
    }

    public int getPhoneNr() {
        return phoneNr;
    }

    public String getPayPal() {
        return payPal;
    }

    public User setPayPal(String payPal) {
        this.payPal = payPal;
        return this;
    }

    public User setAddresses(List<Address> addresses) {
        this.addresses = addresses;
        return this;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public User addAddress(Address address) {
        addresses.add(address);
        return this;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + this.getId() + ", name='" + name + '\'' + ", lastname='" + lastname + '\''
                + ", addresses=" + addresses + ", phoneNr=" + phoneNr + ", payPal='" + payPal + '\'' + '}';
    }
}
