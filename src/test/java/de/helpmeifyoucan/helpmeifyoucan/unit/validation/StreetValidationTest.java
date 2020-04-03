package de.helpmeifyoucan.helpmeifyoucan.unit.validation;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.StreetPatternException;
import de.helpmeifyoucan.helpmeifyoucan.validation.StreetValidator;

public class StreetValidationTest {

    private static StreetValidator validator = new StreetValidator();

    @Test
    public void street_validStreets() {
        assertTrue(validator.isValid("MyStreet", null));
        assertTrue(validator.isValid("Lärmstraße", null));
        assertTrue(validator.isValid("Friedrich-ebert-straße", null));
        assertTrue(validator.isValid("Friedrich ebert straße", null));
    }

    // -------------------------------------------------------------------------
    // Length To Short
    // -------------------------------------------------------------------------

    @Test(expected = StreetPatternException.class)
    public void street_Blank() {
        assertFalse(validator.isValid("", null));
    }

    @Test(expected = StreetPatternException.class)
    public void street_ToShort() {
        assertFalse(validator.isValid("ab", null));
    }

    // -------------------------------------------------------------------------
    // Invalid character
    // -------------------------------------------------------------------------

    @Test(expected = StreetPatternException.class)
    public void street_InvalidCharacter_Numbers() {
        assertFalse(validator.isValid("MyStreet 11", null));
    }

    @Test(expected = StreetPatternException.class)
    public void street_InvalidCharacter_Special() {
        assertFalse(validator.isValid("My$treet", null));
    }
}