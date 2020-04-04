package de.helpmeifyoucan.helpmeifyoucan.security.providers;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import de.helpmeifyoucan.helpmeifyoucan.config.JwtConfig;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.security.authentications.JwtAuthentication;
import de.helpmeifyoucan.helpmeifyoucan.utils.Role;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private JwtParser jwtParser;
    private JwtConfig config;

    @Autowired
    public JwtAuthenticationProvider(JwtConfig config) {
        this.config = config;
        this.jwtParser = Jwts.parserBuilder().setSigningKey(config.getSecret()).build();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var token = authentication.getCredentials().toString();
        return this.authenticateToken(token);

    }

    private JwtAuthentication authenticateToken(String token) throws AuthenticationException {

        try {
            var claims = this.jwtParser.parseClaimsJws(token);
            var jwsBody = claims.getBody();
            var id = new ObjectId(jwsBody.getSubject());
            var roles = (List<String>) jwsBody.get("roles", List.class);
            var authorities = this.rolesToAuthorities(roles);
            return new JwtAuthentication(id, token, authorities);
        } catch (JwtException ex) {
            throw new BadCredentialsException("Token is invalid", ex);
        }
    }

    public String generateToken(Authentication authentication) {

        final Date issuedDate = new Date();
        final Date expirationDate = new Date(issuedDate.getTime() + this.config.getExpiration());
        var user = (UserModel) authentication.getPrincipal();
        var roles = this.authoritiesToRoles(authentication.getAuthorities());

        return Jwts.builder().setSubject(user.getId().toString()).setIssuedAt(issuedDate).setExpiration(expirationDate)
                .claim("roles", roles).signWith(config.getSecret()).compact();
    }

    private List<GrantedAuthority> rolesToAuthorities(List<String> roles) {
        return roles.stream().map(role -> {
            return new SimpleGrantedAuthority(role.toString());
        }).collect(Collectors.toList());
    }

    private List<Role> authoritiesToRoles(Collection<? extends GrantedAuthority> authorities) {

        return authorities.stream().map(authority -> {
            return Role.valueOf(authority.toString());
        }).collect(Collectors.toList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthentication.class);
    }

}