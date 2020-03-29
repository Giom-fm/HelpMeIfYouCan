package de.helpmeifyoucan.helpmeifyoucan;


import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.AddressUpdate;
import de.helpmeifyoucan.helpmeifyoucan.services.AddressService;
import de.helpmeifyoucan.helpmeifyoucan.services.UserService;
import org.apache.tomcat.jni.Address;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AddressServiceTest {


    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private AddressModel testAddress;

    private UserModel testUser;


    @Before
    public void setUpTest() {
        testUser = new UserModel().setName("Marc").setLastName("Jaeger").setPassword(passwordEncoder.encode("password1")).setEmail("test@Mail.de");
        testAddress = new AddressModel().setCountry("Germany").setDistrict("Hamburg").setStreet("testStreet").setZipCode("22391").setHouseNumber("13");

    }
}
