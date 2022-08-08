package com.sogeti.carlease.controllers;

import com.sogeti.carlease.models.JWTRequest;
import com.sogeti.carlease.models.JWTResponse;
import com.sogeti.carlease.services.LoginService;
import com.sogeti.carlease.utils.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*
 * Contains methods for login in the APIs
 */

import java.util.Collection;

@RestController
public class LoginController {

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private LoginService loginService;

    /*
     * A simple test method to check if authentication works
     * @return String in case of successful login
     */
    @GetMapping("/")
    public String home(){
        return "Welcome You are home";
    }

    /*
     * Entry point of the Application before we can get data from any APIs
     * @RequestBody contains credentials required for login
     * @return A JWTResponse Token needed for accessing any API
     */
    @PostMapping("/authenticate")
    public JWTResponse authenticate(@RequestBody JWTRequest jwtRequest) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUserName(),
                            jwtRequest.getPassword())
            );

            final UserDetails userDetails = loginService.loadUserByUsername(jwtRequest.getUserName());
            final String token = jwtUtility.generateToken(userDetails.getUsername(), (Collection<GrantedAuthority>) authentication.getAuthorities());
            return new JWTResponse(token);
        }
        catch (BadCredentialsException exception){
            throw new Exception("INVALID CREDENTIALS", exception);
        }

    }
}
