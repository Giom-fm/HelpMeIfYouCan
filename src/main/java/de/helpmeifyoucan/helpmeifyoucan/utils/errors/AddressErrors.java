package de.helpmeifyoucan.helpmeifyoucan.utils.errors;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractErrors.NotFoundError;

public class AddressErrors {
    public static class AddressNotFoundError extends NotFoundError {

        private static final long serialVersionUID = 7415040101907590277L;

        public AddressNotFoundError(String address) {
            super(String.format("Address \"%s\" not found", address), 123);
        }
    }
}