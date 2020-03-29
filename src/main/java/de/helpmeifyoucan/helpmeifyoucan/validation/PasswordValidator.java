package de.helpmeifyoucan.helpmeifyoucan.validation;

import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidPassword;
import de.helpmeifyoucan.helpmeifyoucan.utils.ErrorMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

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

        String message = "";
        boolean valid = true;
        if (password == null) {
            if (!this.canBeNull) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.PASSWORD_NOT_SET);
            }
            return true;
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

        return true;

    }
}