package de.helpmeifyoucan.helpmeifyoucan.security.filters;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import de.helpmeifyoucan.helpmeifyoucan.handlers.ErrorHandler;

@Component
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private ErrorHandler erorHandler;

    public LoginAuthenticationFailureHandler(ErrorHandler erorHandler) {
        this.erorHandler = erorHandler;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        var error = this.erorHandler.handleAuthenticationExceptions(exception);
        FilterUtils.send(response, error);
    }

}