package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.Phone;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private static final String PHONE_REGEX = "^[0-9]{6,15}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
}