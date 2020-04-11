package de.helpmeifyoucan.helpmeifyoucan.api.services;

import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.UserUpdate;
import de.helpmeifyoucan.helpmeifyoucan.services.UserService;
import de.helpmeifyoucan.helpmeifyoucan.utils.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest extends AbstractControllerTest {

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
    public void GetMe_SimpleFields() throws URISyntaxException {
        String token = super.LoginAndGetToken(testUser.getEmail(), this.password);
        var uri = new URI(this.host + "/user/me");
        var response = super.get(uri, token, UserModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        var user = response.getBody();
        assertEquals(testUser.getName(), user.getName());
        assertEquals(testUser.getLastName(), user.getLastName());
        assertEquals(testUser.getEmail(), user.getEmail());
    }

    @Test
    public void DeleteMe() throws URISyntaxException {
        String token = super.LoginAndGetToken(testUser.getEmail(), this.password);
        var uri = new URI(this.host + "/user/me");

        var response = super.delete(uri, token, UserModel.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(response.hasBody());

        response = super.get(uri, token, UserModel.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void PatchMe_EmptyBody() throws URISyntaxException {
        String token = super.LoginAndGetToken(testUser.getEmail(), this.password);
        var uri = new URI(this.host + "/user/me");

        var response = super.patch(uri, token, null, UserModel.class);
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, response.getStatusCode());
    }

    @Test
    public void PatchMe_AllFieldsAtOnce() throws URISyntaxException {
        String token = super.LoginAndGetToken(testUser.getEmail(), this.password);
        var uri = new URI(this.host + "/user/me");
        var update = new UserUpdate();

        update.setEmail("new@mail.de");
        update.setName("newName");
        update.setLastName("newLastname");
        update.setPhoneNr("01756154879");
        update.setCurrentPassword(this.password);

        var response = super.patch(uri, token, update, UserModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        var user = response.getBody();
    }

    @Test
    public void Me_AddAddress_EmptyBody() throws URISyntaxException {
        String token = super.LoginAndGetToken(testUser.getEmail(), this.password);
        var uri = new URI(this.host + "/user/me/address");

        var response = super.post(uri, token, null, AddressModel.class);
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, response.getStatusCode());
    }

    @Test
    public void Me_AddAddress_Success() throws URISyntaxException {
        String token = super.LoginAndGetToken(testUser.getEmail(), this.password);
        var uri = new URI(this.host + "/user/me/address?lazy=false");
        var newAddress = new AddressModel();
        newAddress.setStreet("MyStreet");
        newAddress.setDistrict("MyDistrict");
        newAddress.setZipCode("12345");
        newAddress.setCountry("MyCountry");
        newAddress.setHouseNumber("1");

        var userResponse = super.post(uri, token, newAddress, UserModel.class);
        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertTrue(userResponse.hasBody());

        var addressResponse = super.get(uri, token, AddressModel.class);
        assertEquals(HttpStatus.OK, addressResponse.getStatusCode());
        assertTrue(addressResponse.hasBody());
        var address = addressResponse.getBody();
        assertEquals(newAddress, address);
    }


    @Test
    public void Me_AddAddress_failed_SomeValuesMissing() throws URISyntaxException {
        String token = super.LoginAndGetToken(testUser.getEmail(), this.password);
        var uri = new URI(this.host + "/user/me/address?lazy=false");
        var newAddress = new AddressModel();
        newAddress.setStreet("MyStreet");
        newAddress.setDistrict("MyDistrict");
       
        var userResponse = super.post(uri, token, newAddress, UserModel.class);
        assertEquals(HttpStatus.BAD_REQUEST, userResponse.getStatusCode());
    }
}