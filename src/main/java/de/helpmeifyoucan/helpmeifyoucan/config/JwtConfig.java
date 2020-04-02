package de.helpmeifyoucan.helpmeifyoucan.config;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;

@Component
@ConfigurationProperties("jwt")
public class JwtConfig {
    private SecretKey secret;
    private long expiration;

    public SecretKey getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

}