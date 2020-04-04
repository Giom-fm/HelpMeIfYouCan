package de.helpmeifyoucan.helpmeifyoucan.utils.errors;


import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions.NotFoundException;
import org.bson.types.ObjectId;

public final class HelpRequestModelExceptions {

    public static class HelpRequestNotFoundException extends NotFoundException {
        private static final long serialVersionUID = 7415040101907590277L;
        private static ErrorCode error = ErrorCode.HELPREQUEST_NOT_FOUND;

        public HelpRequestNotFoundException(ObjectId id) {
            this(id.toString());
        }

        public HelpRequestNotFoundException(String coordinates) {
            super(String.format(error.getMessage(), coordinates), error.getCode());
        }
    }

}
