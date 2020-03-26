package de.helpmeifyoucan.helpmeifyoucan.constraint;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import de.helpmeifyoucan.helpmeifyoucan.constraint.Annotations.Password;
import de.helpmeifyoucan.helpmeifyoucan.utils.ErrorMessages;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    // private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*([a-zäüöß]|[A-ZÄÜÖ])).{8,128}$";
    private static final String LENGTH_REGEX = "^.{8,128}$";
    private static final String NUMERIC_REGEX = "^(?=.*[0-9]).*$";
    private static final String ALPHABETIC_REGEX = "^(?=.*([a-zäüöß]|[A-ZÄÜÖ])).*$";

    private static final Pattern LENGTH_PATTERN = Pattern.compile(LENGTH_REGEX);
    private static final Pattern NUMERIC_PATTERN = Pattern.compile(NUMERIC_REGEX);
    private static final Pattern ALPHABETIC_PATTERN = Pattern.compile(ALPHABETIC_REGEX);

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        String message = "";
        boolean valid = true;
        if (password == null) {
            message = ErrorMessages.PASSWORD_NOT_SET;
            valid = false;
        } else if (!LENGTH_PATTERN.matcher(password).matches()) {
            message = ErrorMessages.PASSWORD_LENGTH;
            valid = false;
        } else if (!NUMERIC_PATTERN.matcher(password).matches()) {
            message = ErrorMessages.PASSWORD_NUMERIC;
            valid = false;
        } else if (!ALPHABETIC_PATTERN.matcher(password).matches()) {
            message = ErrorMessages.PASSWORD_ALPHABETIC;
            valid = false;
        }

        if (!valid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }

        return valid;

    }
}