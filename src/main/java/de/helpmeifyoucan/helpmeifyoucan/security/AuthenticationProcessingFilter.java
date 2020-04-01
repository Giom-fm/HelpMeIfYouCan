package de.helpmeifyoucan.helpmeifyoucan.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.Credentials;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.response.LoginResponse;

public class AuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationManager authenticationManager;
    private JwtAuthenticationProvider jwtAuthentificationProvider;

    public AuthenticationProcessingFilter(AuthenticationManager authenticationManager,
            JwtAuthenticationProvider jwtAuthentificationProvider) {

        super(new AntPathRequestMatcher("/auth/signin", HttpMethod.POST.name()));
        this.authenticationManager = authenticationManager;
        this.jwtAuthentificationProvider = jwtAuthentificationProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            Credentials credentials = new ObjectMapper().readValue(req.getInputStream(), Credentials.class);
            var authenticationAttempt = new EmailPasswordAuthentication(credentials);
            var authenticationResult = authenticationManager.authenticate(authenticationAttempt);
            return authenticationResult;
        } catch (IOException e) {
            // FIXME
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
            Authentication authenticationResult) throws IOException, ServletException {

        if (!(authenticationResult.getPrincipal() instanceof UserModel)) {
            throw new IllegalArgumentException();
        }

        var user = (UserModel) authenticationResult.getPrincipal();
        var token = this.jwtAuthentificationProvider.generateToken(authenticationResult);
        var login = new LoginResponse(user.getName(), user.getLastName(), token);

        res.setStatus(HttpStatus.OK.value());
        res.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        res.getWriter().write(new ObjectMapper().writeValueAsString(login));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        System.out.println("ERROR DU LELLEK");
    }

}