package de.helpmeifyoucan.helpmeifyoucan.unit.validation;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.NamePatternException;
import de.helpmeifyoucan.helpmeifyoucan.validation.NameValidator;

public class NameValidationTest {

    private static NameValidator validator = new NameValidator();

    @Test
    public void validNames() {
        assertTrue(validator.isValid("firstname", null));
        assertTrue(validator.isValid("firstname secondname", null));
        assertTrue(validator.isValid("firstname-secondname", null));
        assertTrue(validator.isValid("ab-ab", null));
    }

    // -------------------------------------------------------------------------
    // Length To Short
    // -------------------------------------------------------------------------

    @Test(expected = NamePatternException.class)
    public void name_Blank() {
        assertFalse(validator.isValid("", null));
    }

    @Test(expected = NamePatternException.class)
    public void firstname_BlankBySpace() {
        assertFalse(validator.isValid(" secondname", null));
    }

    @Test(expected = NamePatternException.class)
    public void firstname_BlankByMinus() {
        assertFalse(validator.isValid("-secondname", null));
    }

    @Test(expected = NamePatternException.class)
    public void secondName_BlankBySpace() {
        assertFalse(validator.isValid("firstname ", null));
    }

    @Test(expected = NamePatternException.class)
    public void secondName_BlankByMinus() {
        assertFalse(validator.isValid("secondname-", null));
    }

    @Test(expected = NamePatternException.class)
    public void firstname_ToShortBySpace() {
        assertFalse(validator.isValid("a secondname", null));
    }

    @Test(expected = NamePatternException.class)
    public void firstname_ToShortByMinus() {
        assertFalse(validator.isValid("a-secondname", null));
    }

    @Test(expected = NamePatternException.class)
    public void secondName_ToShortBySpace() {
        assertFalse(validator.isValid("firstname a", null));
    }

    @Test(expected = NamePatternException.class)
    public void secondName_ToShortByMinus() {
        assertFalse(validator.isValid("secondname-a", null));
    }

    // -------------------------------------------------------------------------
    // Invalid character
    // -------------------------------------------------------------------------

    @Test(expected = NamePatternException.class)
    public void firstName_InvalidCharacter() {
        assertFalse(validator.isValid("f!rstname-secondname", null));
    }

    @Test(expected = NamePatternException.class)
    public void secondName_InvalidCharacter() {
        assertFalse(validator.isValid("firstname-s3condname", null));
    }

}