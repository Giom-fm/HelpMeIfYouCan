package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import de.helpmeifyoucan.helpmeifyoucan.utils.ErrorMessages;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidZipCode;

public class ZipCodeValidator implements ConstraintValidator<ValidZipCode, String> {

    private static final String ZIPCODE_REGEX = "^[1-9][0-9]{4}$";
    private static final Pattern ZIPCODE_PATTERN = Pattern.compile(ZIPCODE_REGEX);
    private boolean canBeNull;

    @Override
    public void initialize(ValidZipCode validCountry) {
        this.canBeNull = validCountry.canBeNull();
    }

    @Override
    public boolean isValid(String zipcode, ConstraintValidatorContext context) {
        if (zipcode == null) {
            if (!this.canBeNull) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.ZIPCODE_NOT_SET);
            }
            return true;
        } else if (!ZIPCODE_PATTERN.matcher(zipcode).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.ZIPCODE_PATTERN_ERROR);
        }
        return true;
    }
}