package de.helpmeifyoucan.helpmeifyoucan.controllers;

import com.mongodb.client.model.Filters;
import de.helpmeifyoucan.helpmeifyoucan.services.UserService;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.Register;
import de.helpmeifyoucan.helpmeifyoucan.utils.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.UserExceptions.UserAlreadyTakenException;

import javax.validation.Valid;
import java.util.Collections;
import java.util.LinkedList;

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

        var hashedPassword = bCryptPasswordEncoder.encode(register.getPassword());
        var roles = new LinkedList<>(Collections.singletonList(Roles.ROLE_USER));

        var user = new UserModel();
        user.setEmail(register.getEmail()).setPassword(hashedPassword);
        user.setName(register.getName()).setLastName(register.getLastName());
        user.setRoles(roles);

        this.userModelController.save(user);
    }
}