package de.helpmeifyoucan.helpmeifyoucan.controllers;

import java.util.Collections;
import java.util.LinkedList;

import javax.validation.Valid;

import com.mongodb.client.model.Filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.Register;
import de.helpmeifyoucan.helpmeifyoucan.services.UserService;
import de.helpmeifyoucan.helpmeifyoucan.utils.Role;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.UserExceptions.UserAlreadyTakenException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userModelController;

    @Autowired
    public AuthController(BCryptPasswordEncoder bCryptPasswordEncoder, UserService userModelController) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userModelController = userModelController;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signUp(@Valid @RequestBody Register register) {
        var filter = Filters.eq("email", register.getEmail());

        if (this.userModelController.getOptional(filter).isPresent()) {
            throw new UserAlreadyTakenException(register.getEmail());
        }

        // FIXME Create with UserSerive. Not manually
        var hashedPassword = bCryptPasswordEncoder.encode(register.getPassword());
        var roles = new LinkedList<>(Collections.singletonList(Role.ROLE_USER));
        var user = new UserModel();
        user.setEmail(register.getEmail()).setPassword(hashedPassword);
        user.setName(register.getName()).setLastName(register.getLastName());
        user.setRoles(roles);
        this.userModelController.save(user);
    }
}