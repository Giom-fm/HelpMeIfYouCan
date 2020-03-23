package de.helpmeifyoucan.helpmeifyoucan.models;


import org.bson.types.ObjectId;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

public class User extends AbstractEntity {

    @NotNull(message = "Please fill in Name")
    private String name;

    @NotNull(message = "Please fill in Lastname")
    private String lastName;

    @Valid
    private List<ObjectId> addresses;

    @NotNull(message = "Please fill in PhoneNumber")
    private int phoneNr;

    private String payPal;

    @Email(message = "Please fill in Email")
    private String email;


    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
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

    public User setAddresses(List<ObjectId> addresses) {
        this.addresses = addresses;
        return this;
    }

    public List<ObjectId> getAddresses() {
        return addresses;
    }


    @Override
    public String toString() {
        return "User{" + "id=" + this.getId() + ", name='" + name + '\'' + ", lastname='" + lastName + '\''
                + ", addresses=" + addresses + ", phoneNr=" + phoneNr + ", payPal='" + payPal + '\'' + '}';

    }
}
