package de.helpmeifyoucan.helpmeifyoucan.security.providers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import de.helpmeifyoucan.helpmeifyoucan.security.authentications.EmailPasswordAuthentication;
import de.helpmeifyoucan.helpmeifyoucan.services.UserService;
import de.helpmeifyoucan.helpmeifyoucan.utils.Role;

@Component
public class EmailPasswordAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder;

    public EmailPasswordAuthenticationProvider(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authenticationAttempt) throws AuthenticationException {
        var email = authenticationAttempt.getName();
        var password = authenticationAttempt.getCredentials().toString();

        try {
            var user = this.userService.getByEmail(email);
            if (!this.passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Credentials are invalid");
            }
            return new EmailPasswordAuthentication(user, this.rolesToAuthoritys(user.getRoles()));
        } catch (Exception ex) {
            throw new BadCredentialsException("Credentials are invalid", ex);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(EmailPasswordAuthentication.class);
    }

    private List<SimpleGrantedAuthority> rolesToAuthoritys(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList());
    }
}