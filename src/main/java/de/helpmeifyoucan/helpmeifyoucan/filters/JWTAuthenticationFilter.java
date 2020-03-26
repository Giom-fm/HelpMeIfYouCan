package de.helpmeifyoucan.helpmeifyoucan.filters;

import com.auth0.jwt.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.bson.types.ObjectId;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import de.helpmeifyoucan.helpmeifyoucan.config.Config;
import de.helpmeifyoucan.helpmeifyoucan.controllers.database.UserModelController;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.Credentials;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.response.Login;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UserModelController userController;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserModelController userController) {
        this.authenticationManager = authenticationManager;
        this.setFilterProcessesUrl("/auth/signin");
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

        var userDetails = ((User) auth.getPrincipal());
        var id = userDetails.getUsername();
        var roles = userDetails.getAuthorities().stream().map(role -> role.toString()).collect(Collectors.toList());

        String token = JWT.create().withSubject(id).withArrayClaim("roles", roles.toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis() + Config.JWT_EXPIRATION_TIME))
                .sign(HMAC512(Config.JWT_SECRET.getBytes()));

        var user = this.userController.get(new ObjectId(id));
        var login = new Login(user.getName(), user.getLastName(), token);

        res.setStatus(HttpStatus.OK.value());
        res.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        res.getWriter().write(new ObjectMapper().writeValueAsString(login));
    }
}