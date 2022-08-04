package com.sogeti.carlease.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JWTUtility implements Serializable {

    private final String AUTH_HEADER_PARAM_NAME = "Authorization";
    private final String AUTH_HEADER_TOKEN_PREFIX = "Bearer";
    private final String SIGNING_KEY = "ASecretKeyToSigYourJWTToken";
    private final Long EXPIRE_IN = 600000L;
    private final String AUTH_HEADER_USERNAME = "admin";
    private final String AUTH_HEADER_PASSWORD = "password";
    private final String AUTH_HEADER_ROLES = "EMPLOYEE";


    public String getToken(HttpServletRequest request) {
        String authToken = request.getHeader(AUTH_HEADER_PARAM_NAME);
        if(Objects.isNull(authToken)) {
            return null;
        }
        return authToken.substring(AUTH_HEADER_TOKEN_PREFIX.length());
    }

    //Get username from the token
    public String getUsernameFromToken(String token) throws Exception {
        String username = null;
        try {
            final Claims claims = Jwts.parser().setSigningKey(SIGNING_KEY.getBytes()).parseClaimsJws(token).getBody();
            username = String.valueOf(claims.get(AUTH_HEADER_USERNAME));
        }
        catch (Exception e) {
            throw new Exception("INVALID JWT TOKEN");
        }
        return username;
    }

    //Validate the token if it has expired or not
    public boolean isValidToken(String token) throws Exception {
        boolean isValid = true;
        try {
            final Claims claims = Jwts.parser().setSigningKey(SIGNING_KEY.getBytes()).parseClaimsJws(token).getBody();
            isValid = !(claims.getExpiration().before(new Date()));
        }
        catch (Exception e) {
            throw new Exception("INVALID JWT TOKEN");
        }

        return isValid;
    }

    //generate token for user
    public String generateToken(String username, Collection<GrantedAuthority> authorities) {
        //Map<String, Object> claims = new HashMap<>();
        //return generateToken(claims, userDetails.getUsername());

        Claims claims = Jwts.claims();
        String roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        claims.put(AUTH_HEADER_USERNAME, username);
        claims.put(AUTH_HEADER_ROLES, roles);
        Date expiration = Date.from(Instant.ofEpochMilli(new Date().getTime()+EXPIRE_IN));
        return Jwts.builder().setClaims(claims).setIssuedAt(new Date()).setExpiration(expiration).signWith(SignatureAlgorithm.HS512, SIGNING_KEY.getBytes()).compact();
    }

}
