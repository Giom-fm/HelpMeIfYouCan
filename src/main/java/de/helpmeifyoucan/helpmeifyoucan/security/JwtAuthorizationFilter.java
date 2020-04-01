package de.helpmeifyoucan.helpmeifyoucan.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private AuthenticationManager authenticationManager;

    @Autowired
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String header = req.getHeader(AUTHORIZATION_HEADER);
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            var token = header.replace(TOKEN_PREFIX, "");
            var jwtAuthenticationAttempt = new JwtAuthentication(token);
            var jwtAuthenticationResult = this.authenticationManager.authenticate(jwtAuthenticationAttempt);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationResult);
        }
        chain.doFilter(req, res);
    }
}