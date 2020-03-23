package de.helpmeifyoucan.helpmeifyoucan.models;

import org.bson.types.ObjectId;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

public class UserModel extends AbstractEntity {

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
    private String password;



    public UserModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public UserModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserModel setPhoneNr(int phoneNr) {
        this.phoneNr = phoneNr;
        return this;
    }

    public int getPhoneNr() {
        return phoneNr;
    }

    public String getPayPal() {
        return payPal;
    }

    public UserModel setPayPal(String payPal) {
        this.payPal = payPal;
        return this;
    }

    public UserModel setAddresses(List<ObjectId> addresses) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
