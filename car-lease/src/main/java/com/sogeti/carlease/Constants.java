package com.sogeti.carlease;

public class Constants {
    private final String AUTH_HEADER_PARAM_NAME = "Authorization";
    private final String AUTH_HEADER_TOKEN_PREFIX = "Bearer";
    private final String SIGNING_KEY = "ASecretKeyToSigYourJWTToken";
    private final Long EXPIRE_IN = 600000L;
    private final String AUTH_HEADER_USERNAME = "admin";
    private final String AUTH_HEADER_PASSWORD = "password";
    private final String AUTH_HEADER_ROLES = "roles";
}
