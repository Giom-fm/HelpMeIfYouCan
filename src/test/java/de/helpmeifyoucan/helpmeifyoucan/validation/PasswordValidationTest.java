package de.helpmeifyoucan.helpmeifyoucan.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.PasswordAlphabeticException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.PasswordLengthException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.PasswordNumericException;

public class PasswordValidationTest {

    private static PasswordValidator validator = new PasswordValidator();

    @Test
    public void password_ValidPasswords() {
        assertTrue(validator.isValid("password1", null));
        assertTrue(validator.isValid("12345678a", null));
        assertTrue(validator.isValid("12345678A", null));
        assertTrue(validator.isValid("bai6OtaiTa", null));
        assertTrue(validator.isValid("aih e6%Loo!4i", null));
    }

    // -------------------------------------------------------------------------
    // Length To Short
    // -------------------------------------------------------------------------

    @Test(expected = PasswordLengthException.class)
    public void password_Blank() {
        assertFalse(validator.isValid("", null));
    }

    @Test(expected = PasswordLengthException.class)
    public void password_ToShort() {
        assertFalse(validator.isValid("abc1", null));
    }

    // -------------------------------------------------------------------------
    // Character missing
    // -------------------------------------------------------------------------

    @Test(expected = PasswordNumericException.class)
    public void password_MissingNumericCharacter() {
        assertFalse(validator.isValid("password", null));
    }

    @Test(expected = PasswordAlphabeticException.class)
    public void password_MissingAlphabeticCharacter() {
        assertFalse(validator.isValid("0123456789", null));
    }

}