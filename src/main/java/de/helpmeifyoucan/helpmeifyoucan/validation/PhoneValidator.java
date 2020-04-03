package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.PhoneNotSetException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.PhonePatternException;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidPhone;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private static final String PHONE_REGEX = "^[0-9]{6,15}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);
    private boolean canBeNull;

    @Override
    public void initialize(ValidPhone validPhone) {
        this.canBeNull = validPhone.canBeNull();
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null) {
            if (!this.canBeNull) {
                throw new PhoneNotSetException();
            }
            return true;
        } else if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new PhonePatternException();
        }
        return true;
    }
}