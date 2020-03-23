package de.helpmeifyoucan.helpmeifyoucan.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.helpmeifyoucan.helpmeifyoucan.controllers.database.UserController;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserController userController;

    public UserDetailsServiceImpl(UserController userController) {
        this.userController = userController;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return null;
    }
}