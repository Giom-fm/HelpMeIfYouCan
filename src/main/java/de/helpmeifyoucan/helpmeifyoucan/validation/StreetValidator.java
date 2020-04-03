package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.StreetNotSetException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.StreetPatternException;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidStreet;

public class StreetValidator implements ConstraintValidator<ValidStreet, String> {

    private static final String STREET_REGEX = "^(([A-ZÄÜÖ]|[a-zäüöß]){3,24}(\\s|-))*([A-ZÄÜÖ]|[a-zäüöß]){3,24}$";
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
                throw new StreetNotSetException();
            }
            return true;
        } else if (!STREET_PATTERN.matcher(street).matches()) {
            throw new StreetPatternException();
        }
        return true;
    }

}