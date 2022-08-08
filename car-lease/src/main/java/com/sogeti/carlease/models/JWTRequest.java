package com.sogeti.carlease.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTRequest {

  private String userName;
  private String password;
  private List<SimpleGrantedAuthority> authorities;
}
