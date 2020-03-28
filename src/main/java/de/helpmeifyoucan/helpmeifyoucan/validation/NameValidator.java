package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.Name;

public class NameValidator implements ConstraintValidator<Name, String> {

    private static final String NAME_REGEX = "^(([A-ZÄÜÖ]|[a-zäüöß]){2,24}(\\s|-))?([A-ZÄÜÖ]|[a-zäüöß]){2,24}$";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }
}