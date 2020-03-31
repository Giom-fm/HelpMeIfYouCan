package de.helpmeifyoucan.helpmeifyoucan.utils.errors;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractErrors.CustomException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractErrors.NotFoundError;

public class UserErrors {

    public static class UserNotFoundError extends NotFoundError {

        private static final long serialVersionUID = 7415040101907590277L;

        public UserNotFoundError(String user) {
            super(String.format("User \"%s\" not found", user), 123);
        }
    }

    public static class UserAlreadyTakenError extends CustomException {

        private static final long serialVersionUID = 7415040101907590277L;

        public UserAlreadyTakenError(String user) {
            super(String.format("User \"%s\" already Taken", user), 123);
        }
    }

}