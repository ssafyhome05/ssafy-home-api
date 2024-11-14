package com.ssafyhome.auth.dto.oauth2response;

import java.util.Map;

public class AdminResponse implements OAuth2Response {

  private final Map<String, Object> attributes;

  public AdminResponse(Map<String, Object> attributes) {

    this.attributes = attributes;
  }

  @Override
  public String getProvider() {
    return "admin";
  }

  @Override
  public String getProviderId() {
    return "admin";
  }

  @Override
  public String getEmail() {
    return "null";
  }

  @Override
  public String getName() {
    return ((Map<String, String>)attributes.get("response")).get("username");
  }

  public String getRole() { return ((Map<String, String>)attributes.get("response")).get("role"); }
}
