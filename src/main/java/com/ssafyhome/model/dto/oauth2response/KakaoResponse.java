package com.ssafyhome.model.dto.oauth2response;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

  private final Map<String, Object> attributes;

  public KakaoResponse(Map<String, Object> attributes) {

    this.attributes = attributes;
  }

  @Override
  public String getProvider() {
    return "kakao";
  }

  @Override
  public String getProviderId() {
    return attributes.get("id").toString();
  }

  @Override
  public String getEmail() {
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    return kakaoAccount.get("email").toString();
  }

  @Override
  public String getName() {
    Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
    return properties.get("nickname").toString();
  }
}
