package com.sogeti.carlease.services;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/*
 * Service class for login
 * The user values are hard coded since we are not using a persistent database
 * The Roles needs to be changed accordingly in order to access APIs
 */
@Service
public class LoginService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Logic to get the user from Database
        return new User("admin", "password", new ArrayList<>() {{
            add(new SimpleGrantedAuthority("BROKER"));
        }});
    }
}
