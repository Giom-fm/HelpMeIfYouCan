package de.helpmeifyoucan.helpmeifyoucan.utils.errors;

import org.bson.types.ObjectId;

public final class ApplicationExceptions {

    public static class ApplicationNotFoundException extends AbstractExceptions.NotFoundException {


        private static final long serialVersionUID = 7415040101907590277L;
        private static ErrorCode error = ErrorCode.APPLICATION_NOT_FOUND;

        public ApplicationNotFoundException(ObjectId application) {
            this(application.toString());
        }

        public ApplicationNotFoundException(String application) {
            super(String.format(error.getMessage(), application), error.getCode());
        }


    }
}
