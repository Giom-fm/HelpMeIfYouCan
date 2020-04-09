package de.helpmeifyoucan.helpmeifyoucan.models.dtos.response;

import java.util.Date;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions.ApiException;

public class ApiError {
    private final String message;
    private final int code;
    private final Date timestamp;
    private final Object data;

    public ApiError(String message, int code) {
        this(message, code, null);
    }

    public ApiError(String message, int code, Object data) {
        this.code = code;
        this.message = message;
        this.timestamp = new Date();
        this.data = data;
    }

    public ApiError(ApiException error) {
        this(error.getMessage(), error.getCode(), error.getData());
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public Object getData() {
        return this.data;
    }
}