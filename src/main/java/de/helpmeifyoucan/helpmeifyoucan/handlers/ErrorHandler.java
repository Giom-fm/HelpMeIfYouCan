package de.helpmeifyoucan.helpmeifyoucan.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import de.helpmeifyoucan.helpmeifyoucan.models.dtos.response.ErrorResponse;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions.NotFoundException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AuthExceptions.PasswordMismatchException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.UserExceptions.UserAlreadyTakenException;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleNotFoundExceptions(NotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public final ResponseEntity<ErrorResponse> handlePasswordMismatchExceptions(PasswordMismatchException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyTakenException.class)
    public final ResponseEntity<ErrorResponse> handleUserAlreadyTakenExceptions(UserAlreadyTakenException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<ErrorResponse> handleAuthenticationExceptions(AuthenticationException ex) {
        var error = new ErrorResponse(ex.getMessage(), 123, null);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        var error = new ErrorResponse("Internal Server Error", 999, null);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}