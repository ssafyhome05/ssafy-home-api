package com.ssafyhome.auth.service;

import com.ssafyhome.auth.exception.ExpiredRefreshException;
import com.ssafyhome.auth.exception.InvalidJwtException;
import com.ssafyhome.auth.dao.RefreshTokenRepository;
import com.ssafyhome.auth.dto.JwtDto;
import com.ssafyhome.auth.entity.RefreshTokenEntity;
import com.ssafyhome.common.util.CookieUtil;
import com.ssafyhome.common.util.JWTUtil;
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

    checkRefreshTokenError(refreshToken);

    String userSeq = jwtUtil.getKey(refreshToken, "userSeq");
    String userEmail = jwtUtil.getKey(refreshToken, "userEmail");
    return setTokens(userSeq, userEmail);
  }

  @Override
  public void saveRefreshTokenToRedis(String refreshToken, String userSeq) {

    refreshTokenRepository.save(new RefreshTokenEntity(refreshToken, userSeq));
  }

  @Override
  public void checkRefreshTokenError(String refreshToken) {

    if (refreshToken == null || refreshToken.equals("no_refresh_token")) {
      throw new InvalidJwtException(refreshToken);
    }

    if (!jwtUtil.getKey(refreshToken, "category").equals("refresh") ||
        !refreshTokenRepository.existsById(refreshToken)
    ) {
      throw new InvalidJwtException(refreshToken);
    }

    try {
      jwtUtil.isExpired(refreshToken);
    } catch (Exception e) {
      throw new ExpiredRefreshException();
    }
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
