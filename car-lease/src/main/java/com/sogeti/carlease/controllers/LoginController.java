package com.sogeti.carlease.controllers;

import com.sogeti.carlease.models.JWTRequest;
import com.sogeti.carlease.models.JWTResponse;
import com.sogeti.carlease.services.LoginService;
import com.sogeti.carlease.utils.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private LoginService loginService;

    @GetMapping("/")
    public String home(){
        System.out.println("** inside home***");
        return "Welcome You are home";
    }

    @PostMapping("/authenticate")
    public JWTResponse authenticate(@RequestBody JWTRequest jwtRequest) throws Exception {
        System.out.println("** inside authenticate***");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUserName(),
                            jwtRequest.getPassword())
            );
        }
        catch (BadCredentialsException exception){
                throw new Exception("INVALID CREDENTIALS", exception);
        }

        final UserDetails userDetails = loginService.loadUserByUsername(jwtRequest.getUserName());
        final String token = jwtUtility.generateToken(userDetails);
        System.out.println("** token generated ***"+token);
        return new JWTResponse(token);
    }

}
