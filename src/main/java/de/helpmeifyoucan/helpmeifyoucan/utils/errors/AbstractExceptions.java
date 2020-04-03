package de.helpmeifyoucan.helpmeifyoucan.utils.errors;

public final class AbstractExceptions {

    public static abstract class CustomException extends RuntimeException {
     
        private static final long serialVersionUID = 722628591402222442L;
        private Object data;
        private int code;

        public CustomException(String message, int code) {
            super(message);
            this.code = code;
        }

        public CustomException(String message, int code, Object data) {
            this(message, code);
            this.data = data;
        }

        public Object getData() {
            return this.data;
        }

        public int getCode() {
            return this.code;
        }
    }

    public static abstract class NotFoundException extends CustomException {

        private static final long serialVersionUID = 2761795526522376099L;

        public NotFoundException(String message, int code) {
            super(message, code);
        }
    }

}
