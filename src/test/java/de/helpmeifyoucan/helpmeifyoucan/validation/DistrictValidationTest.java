package de.helpmeifyoucan.helpmeifyoucan.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.DistrictPatternException;

public class DistrictValidationTest {

    private static DistrictValidator validator = new DistrictValidator();

    @Test
    public void district_validDistricts() {
        assertTrue(validator.isValid("MyDiscrict", null));
        assertTrue(validator.isValid("Börlin", null));
        assertTrue(validator.isValid("My-District", null));
        assertTrue(validator.isValid("My District", null));
    }

    // -------------------------------------------------------------------------
    // Length To Short
    // -------------------------------------------------------------------------

    @Test(expected = DistrictPatternException.class)
    public void district_Blank() {
        assertFalse(validator.isValid("", null));
    }

    @Test(expected = DistrictPatternException.class)
    public void district_ToShort() {
        assertFalse(validator.isValid("a", null));
    }

    // -------------------------------------------------------------------------
    // Invalid character
    // -------------------------------------------------------------------------

    @Test(expected = DistrictPatternException.class)
    public void district_InvalidCharacter_Numbers() {
        assertFalse(validator.isValid("MyDistrict 11", null));
    }

    @Test(expected = DistrictPatternException.class)
    public void district_InvalidCharacter_Special() {
        assertFalse(validator.isValid("MyD!strict", null));
    }
}