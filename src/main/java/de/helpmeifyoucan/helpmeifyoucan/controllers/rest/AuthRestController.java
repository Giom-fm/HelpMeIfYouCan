package de.helpmeifyoucan.helpmeifyoucan.controllers.rest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.helpmeifyoucan.helpmeifyoucan.models.dtos.Credentials;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthRestController(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Credentials credentials) {
        var password = bCryptPasswordEncoder.encode(credentials.getPassword());
        System.out.println(password);
    }
}