package com.sogeti.carlease.services;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Logic to get the user from Database
        return new User("admin", "password", buildSimpleGrantedAuthorities("EMPLOYEE"));
    }

    private static List<SimpleGrantedAuthority> buildSimpleGrantedAuthorities(final String role) {
        return new ArrayList<SimpleGrantedAuthority>() {{add(new SimpleGrantedAuthority(role));}};
    }
}
