package com.ssafyhome.model.service;

import com.ssafyhome.model.dto.auth.JwtDto;
import jakarta.servlet.http.HttpServletRequest;

public interface JWTService {

  JwtDto reissueRefreshToken(String refreshToken);

  void saveRefreshTokenToRedis(String refreshToken, String userSeq);

  String checkRefreshTokenError(String refreshToken);

  JwtDto setTokens(String userSeq, String userEmail);

  String getRefreshToken(HttpServletRequest request);
}
