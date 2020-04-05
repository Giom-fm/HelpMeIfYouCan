package de.helpmeifyoucan.helpmeifyoucan.api.authentication;

import de.helpmeifyoucan.helpmeifyoucan.StaticDbClear;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.Credentials;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.response.LoginResponse;
import de.helpmeifyoucan.helpmeifyoucan.services.UserService;
import de.helpmeifyoucan.helpmeifyoucan.utils.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.LinkedList;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthenticationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StaticDbClear clear;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private UserModel user;
    private String host;

    @Before
    public void setup() {
        clear.clearDb();
        host = "http://localhost:" + port;
        var roles = new LinkedList<Role>();
        roles.add(Role.ROLE_USER);
        user = new UserModel().setName("Name").setLastName("Lastname").setPassword(passwordEncoder.encode("password1"))
                .setEmail("name@domain.com").setRoles(roles);
        this.userService.save(user);
    }


    @Test
    public void signin_success() throws Exception {
        var uri = host + "/auth/signin";
        var credentials = new Credentials(user.getEmail(), "password1");
        RequestEntity<Credentials> request = RequestEntity.post(new URI(uri)).body(credentials);
        var response = this.restTemplate.exchange(request, LoginResponse.class);
        var login = response.getBody();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(login.getName(), user.getName());
        assertEquals(login.getLastName(), user.getLastName());
        assertFalse(login.getToken().isBlank());
    }


    @Test
    public void signin_failed() throws Exception {
        var uri = host + "/auth/signin";
        var credentials = new Credentials(user.getEmail(), "wrongPassword");
        RequestEntity<Credentials> request = RequestEntity.post(new URI(uri)).body(credentials);
        var response = this.restTemplate.exchange(request, LoginResponse.class);
        var login = response.getBody();
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        assertEquals(login.getName(), user.getName());
        assertEquals(login.getLastName(), user.getLastName());
        assertFalse(login.getToken().isBlank());
    }



}
