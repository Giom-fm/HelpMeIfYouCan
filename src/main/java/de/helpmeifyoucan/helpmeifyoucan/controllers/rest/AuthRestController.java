package de.helpmeifyoucan.helpmeifyoucan.controllers.rest;

import de.helpmeifyoucan.helpmeifyoucan.controllers.database.UserModelController;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.Credentials;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.Register;
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

import javax.validation.Valid;

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
    public void signUp(@Valid @RequestBody Register register) {

        if (this.userModelController.getByEmail(register.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        var hashedPassword = bCryptPasswordEncoder.encode(register.getPassword());
        var roles = new LinkedList<Roles>(Arrays.asList(Roles.ROLE_USER));

        var user = new UserModel();
        user.setEmail(register.getEmail()).setPassword(hashedPassword);
        user.setName(register.getName()).setLastName(register.getLastName());
        user.setRoles(roles);

        this.userModelController.save(user);
    }
}