package com.ssafyhome.model.dto.auth;

import com.ssafyhome.model.dto.oauth2response.AdminResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class AdminOAuth2User implements OAuth2User {

  private final AdminResponse adminResponse;

  public AdminOAuth2User(AdminResponse adminResponse) {
    this.adminResponse = adminResponse;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return null;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add((GrantedAuthority) adminResponse::getRole);

    return authorities;
  }

  @Override
  public String getName() {
    return adminResponse.getName();
  }
}
