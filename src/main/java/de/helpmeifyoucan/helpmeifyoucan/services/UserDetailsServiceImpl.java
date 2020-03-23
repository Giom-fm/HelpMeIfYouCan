package de.helpmeifyoucan.helpmeifyoucan.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.helpmeifyoucan.helpmeifyoucan.controllers.database.UserController;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserController userController;

    public UserDetailsServiceImpl(UserController userController) {
        this.userController = userController;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = this.userController.getByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return new User(user.getEmail(), user.getPassword(), emptyList());
    }
}