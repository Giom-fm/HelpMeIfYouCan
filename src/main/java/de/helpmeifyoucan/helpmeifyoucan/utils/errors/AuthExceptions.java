package de.helpmeifyoucan.helpmeifyoucan.utils.errors;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions.BadRequestException;

public final class AuthExceptions {

    public static class PasswordMismatchException extends BadRequestException {

        private static final long serialVersionUID = 7415040101907590277L;
        private static ErrorCode error = ErrorCode.PASSWORD_MISMATCH;

        public PasswordMismatchException() {
            super(error.getMessage(), error.getCode());
        }
    }

}