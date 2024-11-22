package com.ssafyhome.auth.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomAdminDetails implements UserDetails {

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    return authorities;
  }

  @Override
  public String getPassword() {
    return "1234";
  }

  @Override
  public String getUsername() {
    return "ADMIN_03";
  }
}
