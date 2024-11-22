package com.ssafyhome.auth.service;

import com.ssafyhome.auth.dto.JwtDto;
import jakarta.servlet.http.HttpServletRequest;

public interface JWTService {

  JwtDto reissueRefreshToken(String refreshToken);

  void saveRefreshTokenToRedis(String refreshToken, String userSeq);

  void checkRefreshTokenError(String refreshToken);

  JwtDto setTokens(String userSeq, String userEmail, String type);

  String getRefreshToken(HttpServletRequest request);
}
