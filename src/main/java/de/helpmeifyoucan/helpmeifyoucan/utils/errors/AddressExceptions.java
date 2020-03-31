package de.helpmeifyoucan.helpmeifyoucan.utils.errors;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions.NotFoundException;

public class AddressExceptions {

    public static class AddressNotFoundException extends NotFoundException {

        private static final long serialVersionUID = 7415040101907590277L;
        private static ErrorCodes error = ErrorCodes.ADDRESS_NOT_FOUND;

        public AddressNotFoundException(String address) {
            super(String.format(error.getMessage(), address), error.getCode());
        }
    }
}