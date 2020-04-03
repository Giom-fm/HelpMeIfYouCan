package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.HousenumberNotSetException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.HousenumberPatternException;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidHouseNumber;

public class HouseNumberValidator implements ConstraintValidator<ValidHouseNumber, String> {

    private static final String HOUSENUMBER_REGEX = "^[1-9][0-9]{0,2}(\\s)?[a-zA-Z]?$";
    private static final Pattern HOUSENUMBER_PATTERN = Pattern.compile(HOUSENUMBER_REGEX);
    private boolean canBeNull;

    @Override
    public void initialize(ValidHouseNumber validHouseNumber) {
        this.canBeNull = validHouseNumber.canBeNull();
    }

    @Override
    public boolean isValid(String houseNumber, ConstraintValidatorContext context) {
        if (houseNumber == null) {
            if (!this.canBeNull) {
                throw new HousenumberNotSetException();
            }
            return true;
        } else if (!HOUSENUMBER_PATTERN.matcher(houseNumber).matches()) {
            throw new HousenumberPatternException();
        }
        return true;
    }
}