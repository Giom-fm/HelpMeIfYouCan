package de.helpmeifyoucan.helpmeifyoucan.unit.security.providers;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import de.helpmeifyoucan.helpmeifyoucan.StaticDbClear;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.Credentials;
import de.helpmeifyoucan.helpmeifyoucan.security.authentications.EmailPasswordAuthentication;
import de.helpmeifyoucan.helpmeifyoucan.security.providers.EmailPasswordAuthenticationProvider;
import de.helpmeifyoucan.helpmeifyoucan.services.UserService;
import de.helpmeifyoucan.helpmeifyoucan.utils.Role;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
public class EmailPasswordAuthenticationProviderTest {

    @Autowired
    EmailPasswordAuthenticationProvider provider;
    @Autowired
    private UserService userService;
    @Autowired
    private StaticDbClear clear;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private UserModel testUser;

    @Before
    public void setUpTest() {
        this.clear.clearDb();
        var roles = new LinkedList<Role>();
        roles.add(Role.ROLE_USER);
        testUser = new UserModel().setName("Name").setLastName("Lastname")
                .setPassword(passwordEncoder.encode("password1")).setEmail("name@mail.com").setRoles(roles);
        this.userService.save(testUser);
    }

    @Test
    public void supportsAuthentication() {
        assertTrue(this.provider.supports(EmailPasswordAuthentication.class));
    }

    @Test
    public void authenticationSuccessful() {
        var credentials = new Credentials(testUser.getEmail(), "password1");
        var authenticationAttempt = new EmailPasswordAuthentication(credentials);
        var authenticationResult = this.provider.authenticate(authenticationAttempt);
        assertTrue(authenticationResult.isAuthenticated());
    }

    @Test(expected = BadCredentialsException.class)
    public void wrongEmail() {
        var credentials = new Credentials("wrongEmail@domain.de", "password1");
        var authenticationAttempt = new EmailPasswordAuthentication(credentials);
        this.provider.authenticate(authenticationAttempt);
    }

    @Test(expected = BadCredentialsException.class)
    public void wrongPassword() {
        var credentials = new Credentials(testUser.getEmail(), "password1");
        var authenticationAttempt = new EmailPasswordAuthentication(credentials);
        this.provider.authenticate(authenticationAttempt);
    }

}