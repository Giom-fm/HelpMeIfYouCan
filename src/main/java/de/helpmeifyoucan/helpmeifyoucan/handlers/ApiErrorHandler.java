package de.helpmeifyoucan.helpmeifyoucan.handlers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import de.helpmeifyoucan.helpmeifyoucan.controllers.ErrorController;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions.NotFoundException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AuthExceptions.PasswordMismatchException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.UserExceptions.UserAlreadyTakenException;

@RestControllerAdvice
public class ApiErrorHandler  {

    @Autowired
    ErrorController errorController;

    @ExceptionHandler({NotFoundException.class, NoHandlerFoundException.class})
    public final ResponseEntity<Map<String, Object>> handleNotFoundExceptions(HttpServletRequest request) {
        return errorController.handleException(request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ MissingServletRequestParameterException.class, ServletRequestBindingException.class,
        TypeMismatchException.class, HttpMessageNotReadableException.class, MethodArgumentNotValidException.class,
        MissingServletRequestPartException.class, BindException.class,PasswordMismatchException.class })
        public final ResponseEntity<Map<String, Object>> handleBadRequestExceptions(HttpServletRequest request) {
        return errorController.handleException(request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyTakenException.class)
    public final ResponseEntity<Map<String, Object>> handleConflictExceptions(HttpServletRequest request) {
        return errorController.handleException(request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<Map<String, Object>>  handleAuthenticationExceptions(HttpServletRequest request) {
        return errorController.handleException(request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity<Map<String, Object>> handleNotAllowedExceptions(HttpServletRequest request) {
        return errorController.handleException(request, HttpStatus.METHOD_NOT_ALLOWED);
    }


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex,HttpServletRequest request) {
        return errorController.handleException(request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

  
}