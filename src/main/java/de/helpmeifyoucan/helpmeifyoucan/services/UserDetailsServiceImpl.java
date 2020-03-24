package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.model.Filters;
import de.helpmeifyoucan.helpmeifyoucan.controllers.database.UserModelController;
import de.helpmeifyoucan.helpmeifyoucan.utils.Roles;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserModelController userModelController;

    public UserDetailsServiceImpl(UserModelController userModelController) {
        this.userModelController = userModelController;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var optUser = this.userModelController.exists(Filters.eq("email", email));
        if (optUser.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
        var user = optUser.get();
        return new User(user.getId().toString(), user.getPassword(), this.rolesToAuthoritys(user.getRoles()));
    }

    private List<SimpleGrantedAuthority> rolesToAuthoritys(List<Roles> roles) {
        return roles.stream().map(role -> {
            return new SimpleGrantedAuthority(role.toString());
        }).collect(Collectors.toList());
    }
}