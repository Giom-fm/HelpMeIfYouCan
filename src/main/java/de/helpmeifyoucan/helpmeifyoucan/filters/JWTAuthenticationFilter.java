package de.helpmeifyoucan.helpmeifyoucan.filters;

import com.auth0.jwt.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import de.helpmeifyoucan.helpmeifyoucan.config.Config;
import de.helpmeifyoucan.helpmeifyoucan.controllers.database.UserController;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.Credentials;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.Login;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UserController userController;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserController userController) {
        this.authenticationManager = authenticationManager;
        this.setFilterProcessesUrl("/auth/login");
        this.userController = userController;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            Credentials credentials = new ObjectMapper().readValue(req.getInputStream(), Credentials.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(),
                    credentials.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException, ServletException {

        var email = ((User) auth.getPrincipal()).getUsername();

        String token = JWT.create().withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + Config.JWT_EXPIRATION_TIME))
                .sign(HMAC512(Config.JWT_SECRET.getBytes()));

        // res.addHeader(Config.JWT_HEADER_STRING, Config.JWT_TOKEN_PREFIX + token);
        var user = this.userController.getByEmail(email);
        var login = new Login(user.getName(), user.getLastName(), token);
        var json = new ObjectMapper().writeValueAsString(login);
        res.setStatus(HttpStatus.OK.value());
        // FIXME Use Spring Utils to write Header
        res.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        res.getWriter().write(json);
    }
}