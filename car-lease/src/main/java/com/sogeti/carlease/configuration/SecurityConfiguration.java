package com.sogeti.carlease.configuration;

import com.sogeti.carlease.filter.JWTFilter;
import com.sogeti.carlease.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/*
 * Contains the security configuration of the APIs
 */

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private LoginService loginService;

    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*
     * All the calls with proper credentials to the uri "/Autheticate" are permitted by default
     * while "/api/customer/**" can only be accessed by a user with role Broker
     * and "/api/car/**" can only be accessed by a user with role Employee
     */
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return  http.cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/authenticate").permitAll()
                .antMatchers("/api/customer/**").hasAuthority("BROKER")
                .antMatchers("/api/car/**").hasAuthority("EMPLOYEE")
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
