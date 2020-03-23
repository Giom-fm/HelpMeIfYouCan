package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.model.Filters;
import de.helpmeifyoucan.helpmeifyoucan.controllers.database.UserModelController;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserModelController userModelController;

    public UserDetailsServiceImpl(UserModelController userModelController) {
        this.userModelController = userModelController;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = this.userModelController.exists(Filters.eq("email", email));
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
        return new User(user.get().getEmail(), user.get().getPassword(), emptyList());
    }
}