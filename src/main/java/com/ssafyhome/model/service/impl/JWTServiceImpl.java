package com.ssafyhome.model.service.impl;

import com.ssafyhome.exception.InvalidJwtException;
import com.ssafyhome.model.dao.repository.RefreshTokenRepository;
import com.ssafyhome.model.dto.JwtDto;
import com.ssafyhome.model.entity.redis.RefreshTokenEntity;
import com.ssafyhome.model.service.JWTService;
import com.ssafyhome.util.CookieUtil;
import com.ssafyhome.util.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class JWTServiceImpl implements JWTService {

  private final JWTUtil jwtUtil;
  private final CookieUtil cookieUtil;
  private final RefreshTokenRepository refreshTokenRepository;

  public JWTServiceImpl(
      JWTUtil jwtUtil,
      CookieUtil cookieUtil,
      RefreshTokenRepository refreshTokenRepository
  ) {

    this.jwtUtil = jwtUtil;
    this.cookieUtil = cookieUtil;
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Override
  public JwtDto reissueRefreshToken(String refreshToken) {

    String RefreshTokenError = checkRefreshTokenError(refreshToken);
    if (RefreshTokenError != null) {
      throw new InvalidJwtException(RefreshTokenError);
    }

    String userSeq = jwtUtil.getKey(refreshToken, "userSeq");
    String userEmail = jwtUtil.getKey(refreshToken, "userEmail");
    return setTokens(userSeq, userEmail);
  }

  @Override
  public void saveRefreshTokenToRedis(String refreshToken, String userSeq) {

    refreshTokenRepository.save(new RefreshTokenEntity(refreshToken, userSeq));
  }

  @Override
  public String checkRefreshTokenError(String refreshToken) {

    if (refreshToken == null || refreshToken.equals("no_refresh_token")) {
      return "refresh token not found";
    }

    if (!jwtUtil.getKey(refreshToken, "category").equals("refresh") ||
        !refreshTokenRepository.existsById(refreshToken) ||
        jwtUtil.isExpired(refreshToken)
    ) {
      return "invalid refresh token";
    }
    
    return null;
  }

  @Override
  public JwtDto setTokens(String userSeq, String userEmail) {

    String accessToken = jwtUtil.createJWT("access", userSeq, userEmail, 5 * 60 * 1000L);
    String refreshToken = jwtUtil.createJWT("refresh", userSeq, userEmail, 24 * 60 * 60 * 1000L);
    Cookie refreshTokenCookie = cookieUtil.createCookie("refresh", refreshToken);

    saveRefreshTokenToRedis(refreshToken, userSeq);

    return JwtDto.builder()
        .accessToken(accessToken)
        .refreshToken(refreshTokenCookie)
        .build();
  }

  @Override
  public String getRefreshToken(HttpServletRequest request) {

    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("refresh")) return cookie.getValue();
    }
    return null;
  }
}
