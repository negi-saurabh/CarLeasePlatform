package com.sogeti.carlease.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTRequest {

    private String userName;
    private String password;
    private List<SimpleGrantedAuthority> authorities;
}
