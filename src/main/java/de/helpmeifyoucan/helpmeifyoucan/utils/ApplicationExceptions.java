package de.helpmeifyoucan.helpmeifyoucan.utils;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.ErrorCode;
import org.bson.types.ObjectId;

public final class ApplicationExceptions {

    private static ErrorCode error = ErrorCode.APPLICATION_NOT_ALLOWED;


    public static class ApplicationNotPossibleException extends AbstractExceptions.BadRequestException {
        private static final long serialVersionUID = 7415040101907590277L;

        public ApplicationNotPossibleException(String message) {
            super(String.format(message), error.getCode());
        }

    }

    public static class DuplicateApplicationException extends AbstractExceptions.BadRequestException {
        public DuplicateApplicationException(Class<?> modelClass,
                                             ObjectId modelId) {
            super(String.format("You already applied " +
                    "to " + modelClass.getSimpleName() + " id: " + modelId), error.getCode());
        }
    }

    public static class OwnPostApplicationException extends AbstractExceptions.BadRequestException {
        public OwnPostApplicationException(Class<?> modelClass) {
            super(String.format("Application to own " +
                    modelClass.getSimpleName() + " not allowed"), error.getCode());
        }
    }
}
