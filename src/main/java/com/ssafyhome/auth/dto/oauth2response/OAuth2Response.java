package com.ssafyhome.auth.dto.oauth2response;

public interface OAuth2Response {

  String getProvider();
  String getProviderId();
  String getEmail();
  String getName();
}
