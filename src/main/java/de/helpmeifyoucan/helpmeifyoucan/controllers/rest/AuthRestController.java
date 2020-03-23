package de.helpmeifyoucan.helpmeifyoucan.controllers.rest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.helpmeifyoucan.helpmeifyoucan.controllers.database.UserController;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.Credentials;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserController userController;

    public AuthRestController(BCryptPasswordEncoder bCryptPasswordEncoder, UserController userController) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userController = userController;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Credentials credentials) {
        // TODO check Email is already Taken
        var hashedPassword = bCryptPasswordEncoder.encode(credentials.getPassword());
        // var user = new UserModel(credentials.getEmail(), hashedPassword);
        var user = new UserModel();
        user.setEmail(credentials.getEmail());
        user.setPassword(hashedPassword);
        this.userController.save(user);
    }
}