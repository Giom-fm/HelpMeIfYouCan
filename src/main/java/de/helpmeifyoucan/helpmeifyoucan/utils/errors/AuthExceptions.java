package de.helpmeifyoucan.helpmeifyoucan.utils.errors;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions.CustomException;

public class AuthExceptions {

    public static class PasswordMismatchException extends CustomException {

        private static final long serialVersionUID = 7415040101907590277L;
        private static ErrorCodes error = ErrorCodes.PASSWORD_MISMATCH;

        public PasswordMismatchException() {
            super(error.getMessage(), error.getCode());
        }
    }

}