package de.helpmeifyoucan.helpmeifyoucan.api.services;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.services.UserService;
import de.helpmeifyoucan.helpmeifyoucan.utils.Role;


public class AuthenticationTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private UserModel testUser;
    private String password = "password1";

    @Override
    protected void initDB() {
        var roles = new LinkedList<Role>();
        roles.add(Role.ROLE_USER);
        testUser = new UserModel().setName("Name").setLastName("Lastname")
                .setPassword(passwordEncoder.encode(this.password)).setEmail("name@domain.com").setRoles(roles);
        this.userService.save(testUser);

    }
    
    @Test
    public void signin_success() throws Exception {
        var response = super.login(testUser.getEmail(), this.password);
        var login = response.getBody();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(login.getName(), testUser.getName());
        assertEquals(login.getLastName(), testUser.getLastName());
        assertFalse(login.getToken().isBlank());
    }
}
