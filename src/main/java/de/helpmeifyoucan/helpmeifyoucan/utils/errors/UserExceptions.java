package de.helpmeifyoucan.helpmeifyoucan.utils.errors;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions.CustomException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions.NotFoundException;

public class UserExceptions {

    public static class UserNotFoundException extends NotFoundException {

        private static final long serialVersionUID = 7415040101907590277L;
        private static ErrorCodes error = ErrorCodes.USER_NOT_FOUND;

        public UserNotFoundException(String user) {
            super(String.format(error.getMessage(), user), error.getCode());
        }
    }

    public static class UserAlreadyTakenException extends CustomException {

        private static final long serialVersionUID = 7415040101907590277L;
        private static ErrorCodes error = ErrorCodes.USER_TAKEN;

        public UserAlreadyTakenException(String user) {
            super(String.format(error.getMessage(), user), error.getCode());
        }
    }

}