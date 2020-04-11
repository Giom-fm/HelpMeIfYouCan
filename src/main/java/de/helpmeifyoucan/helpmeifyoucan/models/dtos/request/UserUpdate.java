package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;

import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidEmail;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidName;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidPassword;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidPhone;
import org.bson.conversions.Bson;

import javax.validation.constraints.NotNull;

public class UserUpdate extends ModelUpdate {

    @ValidEmail(canBeNull = true)
    protected String email;
    @ValidPassword(canBeNull = true)
    protected String password;
    @ValidName(canBeNull = true)
    protected String name;
    @ValidName(canBeNull = true)
    protected String lastName;
    @ValidPhone(canBeNull = true)
    protected String phoneNr;
    @NotNull
    private String currentPassword;

    public UserUpdate() {

    }

    public String getCurrentPassword() {
        return this.currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public Bson toFilter() {

        this.currentPassword = null;
        return super.toFilter(this);
    }

}
