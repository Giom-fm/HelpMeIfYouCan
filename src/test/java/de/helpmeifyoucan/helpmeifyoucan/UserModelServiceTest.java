package de.helpmeifyoucan.helpmeifyoucan;

import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.services.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UserModelServiceTest {


    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Test
    void saveTest() {
        UserModel testUser = new UserModel().setName("Marc").setLastName("Jaeger").setPassword(passwordEncoder.encode("password1")).setEmail("test@Mail.de");

        this.userService.save(testUser);

        UserModel savedUser = userService.get(testUser.getId());
        assertEquals(savedUser, testUser);
    }

    @Test
    void deleteTest() {
        UserModel testUser = new UserModel().setName("Marc").setLastName("Jaeger").setPassword(passwordEncoder.encode("password1")).setEmail("test@Mail.de");
        this.userService.save(testUser);

        this.userService.delete(testUser.getId());

        //this.userService.getOptional()

    }

    @AfterAll
    void dropCollection() {
        userService.getCollection().drop();
    }


}
