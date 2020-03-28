package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import de.helpmeifyoucan.helpmeifyoucan.utils.ErrorMessages;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidDistrict;

public class DistrictValidator implements ConstraintValidator<ValidDistrict, String> {

    private static final String DISTRICT_REGEX = "^(([A-ZÄÜÖ]|[a-zäüöß]){2,24}(\\s|-))*([A-ZÄÜÖ]|[a-zäüöß]){2,24}$";
    private static final Pattern DISTRICT_PATTERN = Pattern.compile(DISTRICT_REGEX);

    @Override
    public boolean isValid(String district, ConstraintValidatorContext context) {
        if (district == null) {
            return true;
        } else if (!DISTRICT_PATTERN.matcher(district).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.DISTRICT_PATTERN_ERROR);
        }
        return true;
    }
}