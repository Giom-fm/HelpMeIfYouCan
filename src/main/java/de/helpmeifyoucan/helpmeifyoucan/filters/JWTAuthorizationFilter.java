package de.helpmeifyoucan.helpmeifyoucan.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import de.helpmeifyoucan.helpmeifyoucan.config.Config;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final JWTVerifier verifier;

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
        this.verifier = JWT.require(Algorithm.HMAC512(Config.JWT_SECRET.getBytes())).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String header = req.getHeader(Config.JWT_HEADER_STRING);
        if (header != null && header.startsWith(Config.JWT_TOKEN_PREFIX)) {
            var authentication = this.getAuthentication(header);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(req, res);
        return;
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String header) {
        if (header != null) {
            var token = header.replace(Config.JWT_TOKEN_PREFIX, "");
            var decodedJWT = this.verifier.verify(token);
            var user = decodedJWT.getSubject();
            var roles = decodedJWT.getClaim("roles").asList(SimpleGrantedAuthority.class);

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, roles);
            }
        }
        return null;
    }
}