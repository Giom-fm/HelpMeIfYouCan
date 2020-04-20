package de.helpmeifyoucan.helpmeifyoucan.utils.errors;

import org.bson.types.ObjectId;

public class HelpModelExceptions {

    public static class HelpModelNotFoundException extends AbstractExceptions.NotFoundException {
        private static final long serialVersionUID = 7415040101907590277L;
        private static ErrorCode error = ErrorCode.HELPMODEL_NOT_FOUND;

        public HelpModelNotFoundException(ObjectId id) {
            this(id.toString());
        }

        public HelpModelNotFoundException(String id) {
            super(String.format(error.getMessage(), id), error.getCode());
        }


    }
}
