package de.helpmeifyoucan.helpmeifyoucan.unit.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.CountryPatternException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.DistrictPatternException;
import de.helpmeifyoucan.helpmeifyoucan.validation.DistrictValidator;

public class CountryValidationTest {

    private static DistrictValidator validator = new DistrictValidator();

    @Test
    public void country_validCountries() {
        assertTrue(validator.isValid("MyCounrty", null));
        assertTrue(validator.isValid("Dänemark", null));
        assertTrue(validator.isValid("My-Counrty", null));
        assertTrue(validator.isValid("My Counrty", null));
    }

    // -------------------------------------------------------------------------
    // Length To Short
    // -------------------------------------------------------------------------

    @Test(expected = CountryPatternException.class)
    public void country_Blank() {
        assertFalse(validator.isValid("", null));
    }

    @Test(expected = DistrictPatternException.class)
    public void country_ToShort() {
        assertFalse(validator.isValid("a", null));
    }

    // -------------------------------------------------------------------------
    // Invalid character
    // -------------------------------------------------------------------------

    @Test(expected = CountryPatternException.class)
    public void country_InvalidCharacter_Numbers() {
        assertFalse(validator.isValid("MyCountry 11", null));
    }

    @Test(expected = CountryPatternException.class)
    public void country_InvalidCharacter_Special() {
        assertFalse(validator.isValid("MyC@untry", null));
    }
}