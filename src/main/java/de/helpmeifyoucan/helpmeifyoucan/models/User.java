package de.helpmeifyoucan.helpmeifyoucan.models;

import de.helpmeifyoucan.helpmeifyoucan.utils.ClassName;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

public class User extends AbstractEntity{

    @NotNull(message = "Please fill in Name")
    private String name;

    @NotNull(message = "Please fill in Lastname")
    private String lastname;

    @Valid
    private List<Address> addresses;

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

    public User addAddress(Address address)
    {
        addresses.add(address);
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", addresses=" + addresses +
                ", phoneNr=" + phoneNr +
                ", payPal='" + payPal + '\'' +
                '}';
    }

    @Override
    public ClassName getClassName() {
        return ClassName.User;
    }
}
