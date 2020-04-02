package de.helpmeifyoucan.helpmeifyoucan.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.Credentials;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.response.LoginResponse;
import de.helpmeifyoucan.helpmeifyoucan.security.authentications.EmailPasswordAuthentication;
import de.helpmeifyoucan.helpmeifyoucan.security.providers.JwtAuthenticationProvider;

public class AuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationManager authenticationManager;
    private JwtAuthenticationProvider jwtAuthentificationProvider;

    public AuthenticationProcessingFilter(AuthenticationManager authenticationManager,
            JwtAuthenticationProvider jwtAuthentificationProvider, AuthenticationFailureHandler failureHandler) {

        super(new AntPathRequestMatcher("/auth/signin", HttpMethod.POST.name()));
        this.authenticationManager = authenticationManager;
        this.jwtAuthentificationProvider = jwtAuthentificationProvider;
        this.setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            Credentials credentials = new ObjectMapper().readValue(req.getInputStream(), Credentials.class);
            var authenticationAttempt = new EmailPasswordAuthentication(credentials);
            var authenticationResult = authenticationManager.authenticate(authenticationAttempt);
            return authenticationResult;
        } catch (Exception ex) {
            // No need to clear the Security Context. Parent class is cleaning
            throw new BadCredentialsException("Credentials are invalid", ex);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authenticationResult) throws IOException, ServletException {

        if (!(authenticationResult.getPrincipal() instanceof UserModel)) {
            throw new IllegalArgumentException();
        }

        var user = (UserModel) authenticationResult.getPrincipal();
        var token = this.jwtAuthentificationProvider.generateToken(authenticationResult);
        var login = new LoginResponse(user.getName(), user.getLastName(), token);
        var loginResponse = new ResponseEntity<LoginResponse>(login, HttpStatus.OK);
        FilterUtils.send(response, loginResponse);

    }
}