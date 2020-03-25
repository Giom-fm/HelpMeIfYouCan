package de.helpmeifyoucan.helpmeifyoucan.controllers.rest;

import com.mongodb.client.model.Filters;
import de.helpmeifyoucan.helpmeifyoucan.controllers.database.UserModelController;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.Register;
import de.helpmeifyoucan.helpmeifyoucan.utils.ErrorMessages;
import de.helpmeifyoucan.helpmeifyoucan.utils.Roles;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collections;
import java.util.LinkedList;

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
        var filter = Filters.eq("email", register.getEmail());

        if (this.userModelController.getOptional(filter).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.EMAIL_TAKEN);
        }

        var hashedPassword = bCryptPasswordEncoder.encode(register.getPassword());
        var roles = new LinkedList<>(Collections.singletonList(Roles.ROLE_USER));

        var user = new UserModel();
        user.setEmail(register.getEmail()).setPassword(hashedPassword);
        user.setName(register.getName()).setLastName(register.getLastName());
        user.setPhoneNr(Integer.parseInt(register.getphoneNr()));
        user.setRoles(roles);

        this.userModelController.save(user);
    }
}