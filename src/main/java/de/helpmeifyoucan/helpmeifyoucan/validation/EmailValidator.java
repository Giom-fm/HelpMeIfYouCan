package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.EmailNotSetException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.EmailPatternException;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidEmail;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9]+[a-zA-Z\\._-]{0,63}@[a-zA-Z0-9]{1,200}+\\.[a-zA-Z]{2,50}$";
    private static final Pattern EMAIl_PATTERN = Pattern.compile(EMAIL_REGEX);
    private boolean canBeNull;

    @Override
    public void initialize(ValidEmail validEmail) {
        this.canBeNull = validEmail.canBeNull();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            if (!this.canBeNull) {
                throw new EmailNotSetException();
            }
            return true;
        } else if (!EMAIl_PATTERN.matcher(email).matches()) {
            throw new EmailPatternException();
        }

        return true;
        
    }
}