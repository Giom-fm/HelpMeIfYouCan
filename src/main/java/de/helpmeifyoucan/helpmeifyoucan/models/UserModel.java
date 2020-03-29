package de.helpmeifyoucan.helpmeifyoucan.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.UserUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.ListObjectIdMapping;
import de.helpmeifyoucan.helpmeifyoucan.utils.Roles;
import org.bson.types.ObjectId;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UserModel extends AbstractEntity {

    @NotNull(message = "Please fill in Name")
    protected String name;

    @NotNull(message = "Please fill in Lastname")
    protected String lastName;

    @JsonSerialize(converter = ListObjectIdMapping.class)
    @Valid
    protected List<ObjectId> addresses;

    @NotNull(message = "Please fill in PhoneNumber")
    protected int phoneNr;

    protected String payPal;

    @Email(message = "Please fill in Email")
    protected String email;

    @JsonIgnore
    protected String password;

    protected List<Roles> roles;

    protected boolean enabled;

    protected boolean verified;


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
        this.email = email.toLowerCase();
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

    public UserModel mergeWithUserUpdate(UserUpdate update) {
        super.mergeWithUpdate(update, this);
        return this;
    }

    public UserModel setAddresses(List<ObjectId> addresses) {
        this.addresses = addresses;
        return this;
    }

    public UserModel addAddress(ObjectId address) {
        if (this.addresses != null) {
            this.addresses.add(address);
            return this;
        }

        return this.setAddresses(Collections.singletonList(address));
    }

    public boolean noAddressReferences() {
        if (this.addresses == null) {
            return true;
        }
        return this.addresses.isEmpty();

    }

    public UserModel removeAddress(ObjectId address) {
        this.addresses.remove(address);
        return this;
    }

    public List<ObjectId> getAddresses() {
        return this.addresses;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", addresses=" + addresses +
                ", phoneNr=" + phoneNr +
                ", payPal='" + payPal + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", enabled=" + enabled +
                ", verified=" + verified +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public UserModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public ObjectId generateId() {
        this.setId(new ObjectId());
        return this.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return getName().equals(userModel.getName()) &&
                getLastName().equals(userModel.getLastName()) &&
                getEmail().equals(userModel.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getLastName(), getEmail());
    }
}
