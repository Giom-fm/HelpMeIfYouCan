package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.PasswordAlphabeticException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.PasswordLengthException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.PasswordNotSetException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.PasswordNumericException;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidPassword;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String LENGTH_REGEX = "^.{8,128}$";
    private static final String NUMERIC_REGEX = "^(?=.*[0-9]).*$";
    private static final String ALPHABETIC_REGEX = "^(?=.*([a-zäüöß]|[A-ZÄÜÖ])).*$";

    private static final Pattern LENGTH_PATTERN = Pattern.compile(LENGTH_REGEX);
    private static final Pattern NUMERIC_PATTERN = Pattern.compile(NUMERIC_REGEX);
    private static final Pattern ALPHABETIC_PATTERN = Pattern.compile(ALPHABETIC_REGEX);

    private boolean canBeNull;

    @Override
    public void initialize(ValidPassword validPassword) {
        this.canBeNull = validPassword.canBeNull();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null) {
            if (!this.canBeNull) {
                throw new PasswordNotSetException();
            }
            return true;
        } else if (!LENGTH_PATTERN.matcher(password).matches()) {
            throw new PasswordLengthException();
        } else if (!NUMERIC_PATTERN.matcher(password).matches()) {
            throw new PasswordNumericException();
        } else if (!ALPHABETIC_PATTERN.matcher(password).matches()) {
            throw new PasswordAlphabeticException();
        }

        return true;

    }
}