package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.DistrictNotSetException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.DistrictPatternException;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidDistrict;

public class DistrictValidator implements ConstraintValidator<ValidDistrict, String> {

    private static final String DISTRICT_REGEX = "^(([A-ZÄÜÖ]|[a-zäüöß]){2,24}(\\s|-))*([A-ZÄÜÖ]|[a-zäüöß]){2,24}$";
    private static final Pattern DISTRICT_PATTERN = Pattern.compile(DISTRICT_REGEX);

    private boolean canBeNull;

    @Override
    public void initialize(ValidDistrict validDistrict) {
        this.canBeNull = validDistrict.canBeNull();
    }

    @Override
    public boolean isValid(String district, ConstraintValidatorContext context) {
        if (district == null) {
            if (!this.canBeNull) {
                throw new DistrictNotSetException();
            }
            return true;
        } else if (!DISTRICT_PATTERN.matcher(district).matches()) {
            throw new DistrictPatternException();
        }
        return true;
    }
}