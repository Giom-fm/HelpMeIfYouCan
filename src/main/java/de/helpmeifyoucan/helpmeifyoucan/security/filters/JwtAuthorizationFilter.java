package de.helpmeifyoucan.helpmeifyoucan.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import de.helpmeifyoucan.helpmeifyoucan.handlers.ApiErrorHandler;
import de.helpmeifyoucan.helpmeifyoucan.security.authentications.JwtAuthentication;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private ApiErrorHandler errorHandler;
    private AuthenticationManager authenticationManager;

    @Autowired
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, ApiErrorHandler errorHandler) {
        this.authenticationManager = authenticationManager;
        this.errorHandler = errorHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            String header = request.getHeader(AUTHORIZATION_HEADER);
            if (header != null && header.startsWith(TOKEN_PREFIX)) {
                var token = header.replace(TOKEN_PREFIX, "");
                var jwtAuthenticationAttempt = new JwtAuthentication(token);
                var jwtAuthenticationResult = this.authenticationManager.authenticate(jwtAuthenticationAttempt);
                SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationResult);
                
            }
        } catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            var error = this.errorHandler.handleAuthenticationExceptions(request);
            FilterUtils.send(response, error);
        } 
        chain.doFilter(request, response);
        
    }
}