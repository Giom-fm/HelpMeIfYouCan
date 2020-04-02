package de.helpmeifyoucan.helpmeifyoucan.utils.errors;

import org.bson.types.ObjectId;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions.NotFoundException;

public class AddressExceptions {

    public static class AddressNotFoundException extends NotFoundException {

        private static final long serialVersionUID = 7415040101907590277L;
        private static ErrorCode error = ErrorCode.ADDRESS_NOT_FOUND;

        public AddressNotFoundException(ObjectId address) {
            this(address.toString());
        }

        public AddressNotFoundException(String address) {
            super(String.format(error.getMessage(), address), error.getCode());
        }

    }
}