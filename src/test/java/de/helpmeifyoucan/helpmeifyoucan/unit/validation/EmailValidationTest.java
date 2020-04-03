package de.helpmeifyoucan.helpmeifyoucan.unit.validation;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ValidationExceptions.EmailPatternException;
import de.helpmeifyoucan.helpmeifyoucan.validation.EmailValidator;

public class EmailValidationTest {

    private static EmailValidator validator = new EmailValidator();

    @Test
    public void validEmails() {

        assertTrue(validator.isValid("name@domain.com", null));
        assertTrue(validator.isValid("name-subname@domain.com", null));
        assertTrue(validator.isValid("name_subname@domain.com", null));
        assertTrue(validator.isValid("name.subname@domain.com", null));
        assertTrue(validator.isValid("name123@domain.com", null));
        assertTrue(validator.isValid("123name@domain.com", null));
        assertTrue(validator.isValid("a@domain.com", null));
        assertTrue(validator.isValid("a@a.com", null));
        assertTrue(validator.isValid("a@a.ab", null));
    }

    // -------------------------------------------------------------------------
    // Length To Short
    // -------------------------------------------------------------------------
    @Test(expected = EmailPatternException.class)
    public void name_IsBlank() {
        assertFalse(validator.isValid("@domain.com", null));
    }

    @Test(expected = EmailPatternException.class)
    public void domain_IsBlank() {
        assertFalse(validator.isValid("name@.com", null));
    }

    @Test(expected = EmailPatternException.class)
    public void tld_IsBlank() {
        assertFalse(validator.isValid("name@domain.", null));
    }

    @Test(expected = EmailPatternException.class)
    public void tld_IsToShort() {
        assertFalse(validator.isValid("name@domain.c", null));
    }

    // -------------------------------------------------------------------------
    // Invalid character
    // -------------------------------------------------------------------------

    @Test(expected = EmailPatternException.class)
    public void name_InvalidCharacter() {
        assertFalse(validator.isValid("name*@domain.com", null));
    }

    @Test(expected = EmailPatternException.class)
    public void domain_InvalidCharacter() {
        assertFalse(validator.isValid("name@dom%ain.com", null));
    }

    @Test(expected = EmailPatternException.class)
    public void tld_InvalidCharacter() {
        assertFalse(validator.isValid("name@domain.c?m", null));
    }

    // -------------------------------------------------------------------------
    // Missing character
    // -------------------------------------------------------------------------
    @Test(expected = EmailPatternException.class)
    public void email_MissingAt() {
        assertFalse(validator.isValid("namedomain.com", null));
    }

    @Test(expected = EmailPatternException.class)
    public void email_MissingDot() {
        assertFalse(validator.isValid("name@domaincom", null));
    }

    @Test(expected = EmailPatternException.class)
    public void email_Blank() {
        assertFalse(validator.isValid("", null));
    }

}