package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import de.helpmeifyoucan.helpmeifyoucan.utils.ErrorMessages;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidEmail;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String COUNTRY_REGEX = "^[a-zA-Z0-9]+[a-zA-Z\\._-]{0,63}@[a-zA-Z0-9]{1,200}+\\.[a-zA-Z]{2,50}$";
    private static final Pattern COUNTRY_PATTERN = Pattern.compile(COUNTRY_REGEX);
    private boolean canBeNull;

    @Override
    public void initialize(ValidEmail validEmail) {
        this.canBeNull = validEmail.canBeNull();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {

        if (email == null) {
            if (!this.canBeNull) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.EMAIL_NOT_SET);
            }
            return true;
        } else if (!COUNTRY_PATTERN.matcher(email).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.EMAIL_PATTERN_ERROR);
        }
        return true;
    }
}