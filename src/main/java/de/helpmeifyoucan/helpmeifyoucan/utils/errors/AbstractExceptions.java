package de.helpmeifyoucan.helpmeifyoucan.utils.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class AbstractExceptions {

    public static abstract class ApiException extends ResponseStatusException {

        private static final long serialVersionUID = 722628591402222442L;
        private Object data;
        private int code;

        public ApiException(String message, HttpStatus status, int code) {
            super(status, message);
            this.code = code;
        }

        public ApiException(String message, HttpStatus status, int code, Object data) {
            this(message, status, code);
            this.data = data;
        }

        public Object getData() {
            return this.data;
        }

        public int getCode() {
            return this.code;
        }
    }

    public static abstract class NotFoundException extends ApiException {

        private static final long serialVersionUID = 2761795526522376099L;

        public NotFoundException(String message, int code) {
            super(message, HttpStatus.NOT_FOUND, code);
        }
    }

    public static abstract class ConflictException extends ApiException {

        private static final long serialVersionUID = 2761795526522376099L;

        public ConflictException(String message, int code) {
            super(message, HttpStatus.CONFLICT, code);
        }
    }

    public static abstract class BadRequestException extends ApiException {

        private static final long serialVersionUID = 2761795526522376099L;

        public BadRequestException(String message, int code) {
            super(message, HttpStatus.BAD_REQUEST, code);
        }
    }

}
