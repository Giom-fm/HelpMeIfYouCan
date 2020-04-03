package de.helpmeifyoucan.helpmeifyoucan.unit.validation;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.HousenumberPatternException;
import de.helpmeifyoucan.helpmeifyoucan.validation.HousenumberValidator;

public class HousenumberValidationTest {

    private static HousenumberValidator validator = new HousenumberValidator();

    @Test
    public void housenumber_validHousenumbers() {
        assertTrue(validator.isValid("20", null));
        assertTrue(validator.isValid("24a", null));
        assertTrue(validator.isValid("24 a", null));
    }

    // -------------------------------------------------------------------------
    // Length To Short
    // -------------------------------------------------------------------------

    @Test(expected = HousenumberPatternException.class)
    public void housenumber_Blank() {
        assertFalse(validator.isValid("", null));
    }

    @Test(expected = HousenumberPatternException.class)
    public void housenumber_ToShort() {
        assertFalse(validator.isValid("0", null));
    }

    // -------------------------------------------------------------------------
    // Length To Long
    // -------------------------------------------------------------------------

    @Test(expected = HousenumberPatternException.class)
    public void housenumber_ToLong() {
        assertFalse(validator.isValid("12345", null));
    }

    // -------------------------------------------------------------------------
    // Invalid character
    // -------------------------------------------------------------------------

    @Test(expected = HousenumberPatternException.class)
    public void housenumber_InvalidCharacter() {
        assertFalse(validator.isValid("11&", null));
    }
}