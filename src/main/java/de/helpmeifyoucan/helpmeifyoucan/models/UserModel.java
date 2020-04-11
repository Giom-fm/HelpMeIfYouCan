package de.helpmeifyoucan.helpmeifyoucan.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.UserUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.ObjectIdMapping;
import de.helpmeifyoucan.helpmeifyoucan.utils.Role;
import de.helpmeifyoucan.helpmeifyoucan.utils.UserAddressSerializer;
import de.helpmeifyoucan.helpmeifyoucan.utils.UserApplicationsSerializer;
import de.helpmeifyoucan.helpmeifyoucan.utils.listSerializers.ListObjectIdMapping;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidEmail;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidName;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidPhone;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Objects;

public class UserModel extends AbstractEntity {

    @ValidName
    protected String name;

    @ValidName
    protected String lastName;

    @JsonSerialize(converter = ObjectIdMapping.class)
    protected ObjectId userAddress;

    @BsonIgnore
    @JsonSerialize(using = UserAddressSerializer.class)
    protected AddressModel fullAddress;

    @ValidPhone
    protected String phoneNr;

    protected String payPal;

    @ValidEmail
    protected String email;

    // REVIEW @ValidPassword sollt hier probleme machen
    //@ValidPassword
    @JsonIgnore
    protected String password;

    protected List<Role> roles;

    protected boolean enabled;

    protected boolean verified;

    @JsonSerialize(converter = ListObjectIdMapping.class)
    protected List<ObjectId> helpRequests;

    @JsonSerialize(converter = ListObjectIdMapping.class)
    protected List<ObjectId> helpOffers;


    @JsonSerialize(using = UserApplicationsSerializer.class)
    protected List<HelpModelApplication> applications;


    protected List<HelpModelApplication> acceptedApplications;


    public UserModel() {

    }


    public List<HelpModelApplication> getAcceptedApplications() {
        return acceptedApplications;
    }

    public UserModel setAcceptedApplications(List<HelpModelApplication> acceptedApplications) {
        this.acceptedApplications = acceptedApplications;
        return this;
    }

    public List<ObjectId> getHelpRequests() {
        return helpRequests;
    }

    public UserModel setHelpRequests(List<ObjectId> helpRequests) {
        this.helpRequests = helpRequests;
        return this;
    }

    public List<ObjectId> getHelpOffers() {
        return helpOffers;
    }

    public UserModel setHelpOffers(List<ObjectId> helpOffers) {
        this.helpOffers = helpOffers;
        return this;
    }

    public List<HelpModelApplication> getApplications() {
        return applications;
    }

    public UserModel setApplications(List<HelpModelApplication> applications) {
        this.applications = applications;
        return this;
    }

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

    public UserModel setRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    public List<Role> getRoles() {
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

    public UserModel setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
        return this;
    }

    public String getPhoneNr() {
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

    public UserModel setUserAddress(ObjectId userAddress) {
        this.userAddress = userAddress;
        return this;
    }


    public boolean noAddressReference() {
        return this.userAddress == null;

    }


    public UserModel setFullAddress(AddressModel address) {
        this.fullAddress = address;
        return this;
    }

    public AddressModel getFullAddress() {
        return fullAddress;
    }

    public ObjectId getUserAddress() {
        return this.userAddress;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address=" + userAddress +
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
