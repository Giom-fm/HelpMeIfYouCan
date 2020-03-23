package de.helpmeifyoucan.helpmeifyoucan.controllers.rest;

import de.helpmeifyoucan.helpmeifyoucan.controllers.database.UserModelController;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.Credentials;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserModelController userModelController;

    public AuthRestController(BCryptPasswordEncoder bCryptPasswordEncoder, UserModelController userModelController) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userModelController = userModelController;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Credentials credentials) {
        // TODO check Email is already Taken
        var hashedPassword = bCryptPasswordEncoder.encode(credentials.getPassword());
        // var user = new UserModel(credentials.getEmail(), hashedPassword);
        var user = new UserModel();
        user.setEmail(credentials.getEmail());
        user.setPassword(hashedPassword);
        this.userModelController.save(user);
    }
}