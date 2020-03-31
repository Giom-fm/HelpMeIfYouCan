package de.helpmeifyoucan.helpmeifyoucan.utils.errors;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractErrors.CustomException;

public class AuthErrors {

    public static class PasswordMismatchError extends CustomException {
        private static final long serialVersionUID = 7415040101907590277L;
        public PasswordMismatchError() {
            super("Password mismatch", 123);
        }
    }

}