package de.helpmeifyoucan.helpmeifyoucan.utils.errors;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions.NotFoundException;
import org.bson.types.ObjectId;

public final class HelpOfferModelExceptions {

    public static class HelpOfferNotFoundException extends NotFoundException {
        private static final long serialVersionUID = 7415040101907590277L;
        private static ErrorCode error = ErrorCode.HELPOFFER_NOT_FOUND;

        public HelpOfferNotFoundException(ObjectId id) {
            this(id.toString());
        }

        public HelpOfferNotFoundException(String coordinates) {
            super(String.format(error.getMessage(), coordinates), error.getCode());
        }
    }
}
