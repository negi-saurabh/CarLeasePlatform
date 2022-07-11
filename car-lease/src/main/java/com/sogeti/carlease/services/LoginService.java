package com.sogeti.carlease.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LoginService implements UserDetailsService  {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Logic to get the user from Database
        return new User("broker", "password", new ArrayList<>());
    }
}
