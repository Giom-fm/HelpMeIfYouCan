package de.helpmeifyoucan.helpmeifyoucan.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import de.helpmeifyoucan.helpmeifyoucan.models.dtos.response.ErrorResponse;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractErrors.NotFoundError;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AuthErrors.PasswordMismatchError;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundError.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(NotFoundError ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordMismatchError.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(PasswordMismatchError ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> handleAllExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity<>("Fatal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}