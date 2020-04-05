package de.helpmeifyoucan.helpmeifyoucan.unit.security.providers;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import de.helpmeifyoucan.helpmeifyoucan.security.authentications.JwtAuthentication;
import de.helpmeifyoucan.helpmeifyoucan.security.providers.JwtAuthenticationProvider;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
public class JwtAuthenticationProviderTest {

    @Autowired
    JwtAuthenticationProvider provider;

    @Test
    public void supportsAuthentication() {
        assertTrue(this.provider.supports(JwtAuthentication.class));
    }

    @Test(expected = BadCredentialsException.class)
    public void token_Malformed() {
        var token = "asdasdasdasdasd";
        var authentication = new JwtAuthentication(token);
        this.provider.authenticate(authentication);
    }

    @Test(expected = BadCredentialsException.class)
    public void token_Blank() {
        var token = "";
        var authentication = new JwtAuthentication(token);
        this.provider.authenticate(authentication);
    }
}