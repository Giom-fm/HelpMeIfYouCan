package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import de.helpmeifyoucan.helpmeifyoucan.utils.ErrorMessages;
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

        var valid = true;
        context.disableDefaultConstraintViolation();

        if (email == null && !this.canBeNull) {
            context.buildConstraintViolationWithTemplate(ErrorMessages.EMAIL_NOT_SET).addConstraintViolation();
            valid = false;
        } else if (!EMAIl_PATTERN.matcher(email).matches()) {
            context.buildConstraintViolationWithTemplate(ErrorMessages.EMAIL_PATTERN_ERROR).addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}