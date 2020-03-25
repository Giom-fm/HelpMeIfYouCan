package de.helpmeifyoucan.helpmeifyoucan.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.helpmeifyoucan.helpmeifyoucan.utils.Roles;
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

    @JsonIgnore
    private String password;

    private List<Roles> roles;
    
    boolean enabled;

    boolean verified;

    public UserModel setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isVerified() {
        return verified;
    }

    public UserModel setRoles(List<Roles> roles) {
        this.roles = roles;
        return this;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public UserModel setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public UserModel setVerified(boolean verified) {
        this.verified = verified;
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

    public UserModel addAddress(ObjectId address) {
        this.addresses.add(address);
        return this;
    }

    public UserModel removeAddress(ObjectId address) {
        this.addresses.remove(address);
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

    @Override
    public UserModel generateId() {
        this.setId(new ObjectId());
        return this;
    }
}
