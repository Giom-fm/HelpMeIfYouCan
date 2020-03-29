package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import de.helpmeifyoucan.helpmeifyoucan.utils.ErrorMessages;
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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.NAME_NOT_SET);
            }
            return true;
        } else if (!NAME_PATTERN.matcher(name).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.NAME_PATTERN_ERROR);
        }
        return true;
    }
}