package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.NameNotSetException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.NamePatternException;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidName;

public class NameValidator implements ConstraintValidator<ValidName, String> {

    private static final String NAME_REGEX = "^(([A-ZÄÜÖ]|[a-zäüöß]){2,24}(\\s|-))?([A-ZÄÜÖ]|[a-zäüöß]){2,24}$";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);
    private boolean canBeNull;

    @Override
    public void initialize(ValidName validName) {
        this.canBeNull = validName.canBeNull();
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null) {
            if (!this.canBeNull) {
                throw new NameNotSetException();
            }
            return true;
        } else if (!NAME_PATTERN.matcher(name).matches()) {
            throw new NamePatternException();
        }
        return true;
    }
}