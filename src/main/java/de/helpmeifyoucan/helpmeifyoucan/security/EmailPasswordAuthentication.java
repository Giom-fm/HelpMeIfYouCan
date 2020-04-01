package de.helpmeifyoucan.helpmeifyoucan.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.Credentials;

public class EmailPasswordAuthentication implements Authentication {

    private static final long serialVersionUID = -2105474794750369618L;
    private final String password;
    private final String email;
    private final boolean authenticated;
    private final List<? extends GrantedAuthority> authorities;
    private final UserModel user;

    public EmailPasswordAuthentication(UserModel user, List<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorities = authorities;
        this.authenticated = true;
    }

    public EmailPasswordAuthentication(Credentials credentials) {
        this.email = credentials.getEmail();
        this.password = credentials.getPassword();
        this.authorities = null;
        this.user = null;
        this.authenticated = false;
    }

    @Override
    public String getName() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.user;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Authentication must be set over Constructor!");

    }

}