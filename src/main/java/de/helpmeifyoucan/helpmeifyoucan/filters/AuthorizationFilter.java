package de.helpmeifyoucan.helpmeifyoucan.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.server.ResponseStatusException;

import de.helpmeifyoucan.helpmeifyoucan.config.JwtConfig;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private static Logger log = LoggerFactory.getLogger(AuthorizationFilter.class);
    private JWTVerifier verifier;
    private JwtConfig config;

    public AuthorizationFilter(AuthenticationManager authManager, JwtConfig config) {
        super(authManager);
        this.config = config;
        this.verifier = JWT.require(Algorithm.HMAC512(this.config.getSecret().getBytes())).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer")) {
            var token = header.replace("Bearer ", "");
            var authentication = this.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {

        try {
            var decodedJWT = this.verifier.verify(token);
            var user = decodedJWT.getSubject();
            var roles = decodedJWT.getClaim("roles").asList(SimpleGrantedAuthority.class);
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, roles);
            }
        } catch (TokenExpiredException exception) {
            log.warn("Token Expired");
        } catch (AlgorithmMismatchException exception) {
            log.warn("Wrong Algo");
        } catch (SignatureVerificationException exception) {
            log.warn("Signature");
        } catch (InvalidClaimException exception) {
            log.warn("Claim");
        }
        return null;
    }
}