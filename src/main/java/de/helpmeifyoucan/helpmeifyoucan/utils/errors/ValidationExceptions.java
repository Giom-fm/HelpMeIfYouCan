package de.helpmeifyoucan.helpmeifyoucan.utils.errors;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions.CustomException;

public class ValidationExceptions {

    // -------------------------------------------------------------------------
    // Email
    // -------------------------------------------------------------------------

    public static class EmailNotSetException extends CustomException {

        private static final long serialVersionUID = -3172984630237070428L;
        private static ErrorCode error = ErrorCode.EMAIL_NOT_SET;

        public EmailNotSetException() {
            super(error.getMessage(), error.getCode());
        }
    }

    public static class EmailPatternException extends CustomException {

        private static final long serialVersionUID = -6331924738483294846L;
        private static ErrorCode error = ErrorCode.EMAIL_PATTERN;

        public EmailPatternException() {
            super(error.getMessage(), error.getCode());
        }
    }

    // -------------------------------------------------------------------------
    // Password
    // -------------------------------------------------------------------------

    public static class PasswordNotSetException extends CustomException {
        private static final long serialVersionUID = -5353045296336114156L;
        private static ErrorCode error = ErrorCode.PASSWORD_NOT_SET;

        public PasswordNotSetException() {
            super(error.getMessage(), error.getCode());
        }
    }

    public static class PasswordLengthException extends CustomException {

        private static final long serialVersionUID = -7494105551864856390L;
        private static ErrorCode error = ErrorCode.PASSWORD_LENGTH;

        public PasswordLengthException() {
            super(error.getMessage(), error.getCode());
        }
    }

    public static class PasswordNumericException extends CustomException {

        private static final long serialVersionUID = 1057657194607606595L;
        private static ErrorCode error = ErrorCode.PASSWORD_NUMERIC;

        public PasswordNumericException() {
            super(error.getMessage(), error.getCode());
        }
    }

    public static class PasswordAlphabeticException extends CustomException {

        private static final long serialVersionUID = 6956942502657110107L;
        private static ErrorCode error = ErrorCode.PASSWORD_ALPHABETIC;

        public PasswordAlphabeticException() {
            super(error.getMessage(), error.getCode());
        }
    }

    // -------------------------------------------------------------------------
    // Name
    // -------------------------------------------------------------------------

    public static class NameNotSetException extends CustomException {
        private static final long serialVersionUID = 3359601043972354459L;
        private static ErrorCode error = ErrorCode.NAME_NOT_SET;

        public NameNotSetException() {
            super(error.getMessage(), error.getCode());
        }
    }

    public static class NamePatternException extends CustomException {
        private static final long serialVersionUID = 6834858514021347703L;
        private static ErrorCode error = ErrorCode.NAME_PATTERN;

        public NamePatternException() {
            super(error.getMessage(), error.getCode());
        }
    }

    // -------------------------------------------------------------------------
    // Phone
    // -------------------------------------------------------------------------

    public static class PhoneNotSetException extends CustomException {
        private static final long serialVersionUID = 373301589877299045L;
        private static ErrorCode error = ErrorCode.PHONE_NOT_SET;

        public PhoneNotSetException() {
            super(error.getMessage(), error.getCode());
        }
    }

    public static class PhonePatternException extends CustomException {
        private static final long serialVersionUID = -7180458561889614227L;
        private static ErrorCode error = ErrorCode.PHONE_PATTERN;

        public PhonePatternException() {
            super(error.getMessage(), error.getCode());
        }
    }

    // -------------------------------------------------------------------------
    // Street
    // -------------------------------------------------------------------------

    public static class StreetNotSetException extends CustomException {
        private static final long serialVersionUID = -6497746717167473335L;
        private static ErrorCode error = ErrorCode.STREET_NOT_SET;

        public StreetNotSetException() {
            super(error.getMessage(), error.getCode());
        }
    }

    public static class StreetPatternException extends CustomException {
        private static final long serialVersionUID = 1L;
        private static ErrorCode error = ErrorCode.STREET_PATTERN;

        public StreetPatternException() {
            super(error.getMessage(), error.getCode());
        }
    }

    // -------------------------------------------------------------------------
    // District
    // -------------------------------------------------------------------------

    public static class DistrictNotSetException extends CustomException {
        private static final long serialVersionUID = 1439620932606773910L;
        private static ErrorCode error = ErrorCode.DISTRICT_NOT_SET;

        public DistrictNotSetException() {
            super(error.getMessage(), error.getCode());
        }
    }

    public static class DistrictPatternException extends CustomException {
        private static final long serialVersionUID = -1934984870811366793L;
        private static ErrorCode error = ErrorCode.DISTRICT_PATTERN;

        public DistrictPatternException() {
            super(error.getMessage(), error.getCode());
        }
    }

    // -------------------------------------------------------------------------
    // Housenumber
    // -------------------------------------------------------------------------

    public static class HousenumberNotSetException extends CustomException {
        private static final long serialVersionUID = -6462397022132931425L;
        private static ErrorCode error = ErrorCode.HOUSENUMBER_NOT_SET;

        public HousenumberNotSetException() {
            super(error.getMessage(), error.getCode());
        }
    }

    public static class HousenumberPatternException extends CustomException {
        private static final long serialVersionUID = 3695911556177084350L;
        private static ErrorCode error = ErrorCode.HOUSENUMBER_PATTERN;

        public HousenumberPatternException() {
            super(error.getMessage(), error.getCode());
        }
    }

    // -------------------------------------------------------------------------
    // Zipcode
    // -------------------------------------------------------------------------

    public static class ZipcodeNotSetException extends CustomException {
        private static final long serialVersionUID = 511180654106367902L;
        private static ErrorCode error = ErrorCode.ZIPCODE_NOT_SET;

        public ZipcodeNotSetException() {
            super(error.getMessage(), error.getCode());
        }
    }

    public static class ZipcodePatternException extends CustomException {
        private static final long serialVersionUID = -7400300661057679412L;
        private static ErrorCode error = ErrorCode.ZIPCODE_PATTERN;

        public ZipcodePatternException() {
            super(error.getMessage(), error.getCode());
        }
    }

    // -------------------------------------------------------------------------
    // Country
    // -------------------------------------------------------------------------

    public static class CountryNotSetException extends CustomException {
        private static final long serialVersionUID = 8529832163963998377L;
        private static ErrorCode error = ErrorCode.COUNTRY_NOT_SET;

        public CountryNotSetException() {
            super(error.getMessage(), error.getCode());
        }
    }

    public static class CountryPatternException extends CustomException {
        private static final long serialVersionUID = 2893879540551575616L;
        private static ErrorCode error = ErrorCode.COUNTRY_PATTERN;

        public CountryPatternException() {
            super(error.getMessage(), error.getCode());
        }
    }

}