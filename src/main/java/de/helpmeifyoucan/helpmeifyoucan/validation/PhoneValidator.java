package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import de.helpmeifyoucan.helpmeifyoucan.utils.ErrorMessages;
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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.PHONE_NOT_SET);
            }
            return true;
        } else if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.PHONE_PATTERN_ERROR);
        }
        return true;
    }
}