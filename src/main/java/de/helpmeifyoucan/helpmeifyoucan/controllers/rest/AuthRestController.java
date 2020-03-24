package de.helpmeifyoucan.helpmeifyoucan.controllers.rest;

import de.helpmeifyoucan.helpmeifyoucan.controllers.database.UserModelController;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.Credentials;
import de.helpmeifyoucan.helpmeifyoucan.utils.Roles;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.LinkedList;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserModelController userModelController;

    public AuthRestController(BCryptPasswordEncoder bCryptPasswordEncoder, UserModelController userModelController) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userModelController = userModelController;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signUp(@RequestBody Credentials credentials) {

        if (this.userModelController.getByEmail(credentials.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        var hashedPassword = bCryptPasswordEncoder.encode(credentials.getPassword());
        var user = new UserModel();
        user.setEmail(credentials.getEmail()).setPassword(hashedPassword);
        var roles = new LinkedList<Roles>(Arrays.asList(Roles.ROLE_USER));
        user.setRoles(roles);
        this.userModelController.save(user);
    }
}