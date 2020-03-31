package de.helpmeifyoucan.helpmeifyoucan.models.dtos.response;

import java.util.Date;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractErrors.CustomException;

public class ErrorResponse {
    private final String message;
    private final int code;
    private final Date timestamp;
    private final Object data;

    public ErrorResponse(CustomException error) {
        this.code = error.getCode();
        this.message = error.getMessage();
        this.timestamp = new Date();
        this.data = error.getData();
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