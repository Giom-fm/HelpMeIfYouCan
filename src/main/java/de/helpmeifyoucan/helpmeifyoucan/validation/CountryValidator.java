package de.helpmeifyoucan.helpmeifyoucan.validation;

import de.helpmeifyoucan.helpmeifyoucan.utils.ErrorMessages;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidCountry;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.COUNTRY_NOT_SET);
            }
            return true;
        } else if (!COUNTRY_PATTERN.matcher(country).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.COUNTRY_PATTERN_ERROR);
        }
        return true;
    }
}