package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.CountryNotSetException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.CountryPatternException;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidCountry;

public class CountryValidator implements ConstraintValidator<ValidCountry, String> {

    private static final String COUNTRY_REGEX = "^(([A-ZÄÜÖ]|[a-zäüöß]){2,24}(\\s|-))*([A-ZÄÜÖ]|[a-zäüöß]){2,24}$";
    private static final Pattern COUNTRY_PATTERN = Pattern.compile(COUNTRY_REGEX);
    private boolean canBeNull;

    @Override
    public void initialize(ValidCountry validCountry) {
        this.canBeNull = validCountry.canBeNull();
    }

    @Override
    public boolean isValid(String country, ConstraintValidatorContext context) {

        if (country == null) {
            if (!this.canBeNull) {
                throw new CountryNotSetException();
            }
            return true;
        } else if (!COUNTRY_PATTERN.matcher(country).matches()) {
            throw new CountryPatternException();
        }
        return true;
    }
}