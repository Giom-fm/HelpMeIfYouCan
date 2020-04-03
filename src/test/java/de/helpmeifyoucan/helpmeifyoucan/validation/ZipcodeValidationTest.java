package de.helpmeifyoucan.helpmeifyoucan.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.PhonePatternException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.ZipcodePatternException;

public class ZipcodeValidationTest {

    private static ZipCodeValidator validator = new ZipCodeValidator();

    @Test
    public void zipcode_validZipcodes() {
        assertTrue(validator.isValid("10000", null));
        assertTrue(validator.isValid("10489", null));
    }

    // -------------------------------------------------------------------------
    // Length To Short
    // -------------------------------------------------------------------------

    @Test(expected = ZipcodePatternException.class)
    public void zipcode_Blank() {
        assertFalse(validator.isValid("", null));
    }

    @Test(expected = PhonePatternException.class)
    public void zipcode_ToShort() {
        assertFalse(validator.isValid("1234", null));
    }

    // -------------------------------------------------------------------------
    // Length To Long
    // -------------------------------------------------------------------------

    @Test(expected = ZipcodePatternException.class)
    public void zipcode_ToLong() {
        assertFalse(validator.isValid("1234567890123456", null));
    }

    // -------------------------------------------------------------------------
    // Invalid character
    // -------------------------------------------------------------------------

    @Test(expected = ZipcodePatternException.class)
    public void zipcode_InvalidCharacter() {
        assertFalse(validator.isValid("1123A", null));
    }

    @Test(expected = ZipcodePatternException.class)
    public void zipcode_InvalidCharacter_BeginingWithNull() {
        assertFalse(validator.isValid("01234", null));
    }

}