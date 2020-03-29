package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import de.helpmeifyoucan.helpmeifyoucan.utils.ErrorMessages;

import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidStreet;

public class StreetValidator implements ConstraintValidator<ValidStreet, String> {

    private static final String STREET_REGEX = "^(([A-ZÄÜÖ]|[a-zäüöß]){2,24}(\\s|-))*([A-ZÄÜÖ]|[a-zäüöß]){2,24}$";
    private static final Pattern STREET_PATTERN = Pattern.compile(STREET_REGEX);
    private boolean canBeNull;

    @Override
    public void initialize(ValidStreet validStreet) {
        this.canBeNull = validStreet.canBeNull();
    }

    @Override
    public boolean isValid(String street, ConstraintValidatorContext context) {
        if (street == null) {
            if (!this.canBeNull) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.STREET_NOT_SET);
            }
            return true;
        } else if (!STREET_PATTERN.matcher(street).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.STREET_PATTERN_ERROR);
        }
        return true;
    }

}