package de.helpmeifyoucan.helpmeifyoucan.utils.errors;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions.CustomException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions.NotFoundException;
import org.bson.types.ObjectId;

public final class UserExceptions {

    public static class UserNotFoundException extends NotFoundException {

        private static final long serialVersionUID = 7415040101907590277L;
        private static ErrorCode error = ErrorCode.USER_NOT_FOUND;

        public UserNotFoundException(ObjectId id) {
            this(id.toString());
        }

        public UserNotFoundException(String user) {
            super(String.format(error.getMessage(), user), error.getCode());
        }

    }

    public static class UserAlreadyTakenException extends CustomException {

        private static final long serialVersionUID = 7415040101907590277L;
        private static ErrorCode error = ErrorCode.USER_TAKEN;

        public UserAlreadyTakenException(String user) {
            super(String.format(error.getMessage(), user), error.getCode());
        }
    }

    public static class UserNotFoundByEmailException extends NotFoundException {
        private static final long serialVersionUID = 7415040101907590277L;
        private static ErrorCode error = ErrorCode.USER_EMAIL_NOT_FOUND;

        public UserNotFoundByEmailException(String email) {
            super(String.format(error.getMessage(), email), error.getCode());
        }


    }

}