package de.helpmeifyoucan.helpmeifyoucan.security.authentications;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthentication implements Authentication {

    private static final long serialVersionUID = 8265789418513754940L;
    private final String token;
    private final ObjectId id;
    private final boolean authenticated;
    private final List<GrantedAuthority> authorities;

    public JwtAuthentication(ObjectId id, String token, List<GrantedAuthority> authorities) {
        this.id = id;
        this.token = token;
        this.authorities = authorities;
        this.authenticated = true;
    }

    public JwtAuthentication(String token) {
        this.id = null;
        this.token = token;
        this.authorities = null;
        this.authenticated = false;
    }

    @Override
    public String getName() {
        return this.id.toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.id;
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