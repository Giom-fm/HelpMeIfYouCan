package de.helpmeifyoucan.helpmeifyoucan.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import de.helpmeifyoucan.helpmeifyoucan.models.dtos.response.ErrorResponse;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions.NotFoundException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AuthExceptions.PasswordMismatchException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.UserExceptions.UserAlreadyTakenException;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleNotFoundExceptions(NotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public final ResponseEntity<ErrorResponse> handlePasswordMismatchExceptions(PasswordMismatchException ex,
            WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyTakenException.class)
    public final ResponseEntity<ErrorResponse> handleUserAlreadyTakenExceptions(UserAlreadyTakenException ex,
            WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> handleAllExceptions(Exception ex, WebRequest request) {
        System.out.println(ex);
        return new ResponseEntity<>("Fatal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}