
package de.helpmeifyoucan.helpmeifyoucan.utils.errors;

public enum ErrorCode {
    // -------------------------------------------------------------------------
    // User
    // -------------------------------------------------------------------------

    /**
     * {@code 400 - User \"%s\" not found}
     */
    USER_NOT_FOUND(104, "User \"%s\" not found"),
    /**
     * {@code 400 - User \"%s\" already taken}
     */
    USER_TAKEN(109, "User \"%s\" already taken"),

    /**
     * {@code 400 - User \"%s\" not found}
     */
    USER_EMAIL_NOT_FOUND(104, "UserEmail \"%s\" not found"),

    // -------------------------------------------------------------------------
    // Address
    // -------------------------------------------------------------------------

    /**
     * {@code 400 -  Address \"%s\" not found}
     */
    ADDRESS_NOT_FOUND(204, "Address \"%s\" not found"),

    // -------------------------------------------------------------------------
    // Authentication
    // -------------------------------------------------------------------------

    /**
     * {@code 400 - Password mismatch}
     */
    PASSWORD_MISMATCH(300, "Password mismatch"),

    // -------------------------------------------------------------------------
    // Validation
    // -------------------------------------------------------------------------

    /**
     * {@code 400 - Email is needed}
     */
    EMAIL_NOT_SET(400, "Email is needed"),
    /**
     * {@code 301 - Email is in invalid format}
     */
    EMAIL_PATTERN(401, "Email is in invalid format"),

    // -------------------------------------------------------------------------
    // Password
    // -------------------------------------------------------------------------

    /**
     * {@code 301 - "Password is needed"}
     */
    PASSWORD_NOT_SET(410, "Password is needed"),
    /**
     * {@code 301 - "Password must contain 8-128 characters"}
     */
    PASSWORD_LENGTH(411, "Password must contain 8-128 characters"),
    /**
     * {@code 301 - "Password must contain at least one numeric character"}
     */
    PASSWORD_NUMERIC(412, "Password must contain at least one numeric character"),
    /**
     * {@code 301 - "Password must contain at least one alphabetic character"}
     */
    PASSWORD_ALPHABETIC(413, "Password must contain at least one alphabetic character"),

    // -------------------------------------------------------------------------
    // Name
    // -------------------------------------------------------------------------

    /**
     * {@code 301 - "Name is needed"}
     */
    NAME_NOT_SET(420, "Name is needed"),
    /**
     * {@code 301 - "Name has to be Alphabetic"}
     */
    NAME_PATTERN(421, "Name has to be Alphabetic"),

    // -------------------------------------------------------------------------
    // Phone
    // -------------------------------------------------------------------------

    /**
     * {@code 301 - "Phone is needed"}
     */
    PHONE_NOT_SET(430, "Phone is needed"),
    /**
     * {@code 301 - "Phone has to be numeric"}
     */
    PHONE_PATTERN(431, "Phone has to be numeric"),

    // -------------------------------------------------------------------------
    // Street
    // -------------------------------------------------------------------------

    /**
     * {@code 301 - "Street is needed"}
     */
    STREET_NOT_SET(440, "Street is needed"),
    /**
     * {@code 301 - "Street is in invalid format"}
     */
    STREET_PATTERN(441, "Street is in invalid format"),

    // -------------------------------------------------------------------------
    // District
    // -------------------------------------------------------------------------

    /**
     * {@code 301 - District is needed}
     */
    DISTRICT_NOT_SET(450, "District is needed"),
    /**
     * {@code 301 - "District is in invalid format"}
     */
    DISTRICT_PATTERN(451, "District is in invalid format"),

    // -------------------------------------------------------------------------
    // Housenumber
    // -------------------------------------------------------------------------

    /**
     * {@code 301 - "Housenumber is needed"}
     */
    HOUSENUMBER_NOT_SET(460, "Housenumber is needed"),
    /**
     * {@code 301 - "Housenumber is in invalid format"}
     */
    HOUSENUMBER_PATTERN(461, "Housenumber is in invalid format"),

    // -------------------------------------------------------------------------
    // Zipcode
    // -------------------------------------------------------------------------

    /**
     * {@code 301 - "Zipcode is needed"}
     */
    ZIPCODE_NOT_SET(470, "Zipcode is needed"),
    /**
     * {@code 301 - "Zipcode has to be numeric"}
     */
    ZIPCODE_PATTERN(471, "Zipcode has to be numeric"),

    // -------------------------------------------------------------------------
    // Country
    // -------------------------------------------------------------------------

    /**
     * {@code 301 - "Country is needed"}
     */
    COUNTRY_NOT_SET(480, "Country is needed"),
    /**
     * {@code 301 - "Country is in invalid format"}
     */
    COUNTRY_PATTERN(481, "Country is in invalid format")

    //
    ;

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}