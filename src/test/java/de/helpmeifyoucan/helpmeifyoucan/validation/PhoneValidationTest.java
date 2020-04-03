package de.helpmeifyoucan.helpmeifyoucan.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.PhonePatternException;

public class PhoneValidationTest {

    private static PhoneValidator validator = new PhoneValidator();

    @Test
    public void phone_validPhone() {
        assertTrue(validator.isValid("012345", null));
        assertTrue(validator.isValid("012345678910", null));
    }

    // -------------------------------------------------------------------------
    // Length To Short
    // -------------------------------------------------------------------------

    @Test(expected = PhonePatternException.class)
    public void phone_Blank() {
        assertFalse(validator.isValid("", null));
    }

    @Test(expected = PhonePatternException.class)
    public void phone_ToShort() {
        assertFalse(validator.isValid("1234", null));
    }

    // -------------------------------------------------------------------------
    // Length To Long
    // -------------------------------------------------------------------------

    @Test(expected = PhonePatternException.class)
    public void phone_ToLong() {
        assertFalse(validator.isValid("1234567890123456", null));
    }

    // -------------------------------------------------------------------------
    // Invalid character
    // -------------------------------------------------------------------------

    @Test(expected = PhonePatternException.class)
    public void phone_InvalidCharacter() {
        assertFalse(validator.isValid("0123A", null));
    }

}