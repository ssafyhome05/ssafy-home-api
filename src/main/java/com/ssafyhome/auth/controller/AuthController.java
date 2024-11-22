package com.ssafyhome.auth.controller;

import com.ssafyhome.auth.dto.JwtDto;
import com.ssafyhome.auth.response.AuthResponseCode;
import com.ssafyhome.auth.service.JWTService;
import com.ssafyhome.common.response.ResponseMessage;
import com.ssafyhome.common.util.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "Auth Controller",
    description = "RefreshToken 재발급"
)
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final JWTService jwtService;
  private final CookieUtil cookieUtil;

  public AuthController(
      JWTService jwtService,
      CookieUtil cookieUtil
  ) {

    this.jwtService = jwtService;
    this.cookieUtil = cookieUtil;
  }
  
  
  
  
  @Operation(
      summary = "Refresh Token 재발급",
      description = "### 사용자의 refreshToken 을 활용해 accessToken을 재발급합니다. \n\n"
      		+ "1. 사용자 Header에  Authorization에 갱신된 access token 추가 \n\n"
      		+ "2. Cookie에 갱신된 refresh token 추가"
  )
  @ApiResponses({
	  @ApiResponse(responseCode = "201", description="accessToken 재발급 완료")
  })
  @PostMapping("/reissue")
  public ResponseEntity<?> reissue(
      @Parameter(
          name = "refresh token",
          description = "JWT Refresh Token"
      )
      @CookieValue(value = "refresh", defaultValue = "no_refresh_token")
      String refreshToken
  ) {

    JwtDto jwtDto = jwtService.reissueRefreshToken(refreshToken);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtDto.getAccessToken());
    headers.add(HttpHeaders.SET_COOKIE, cookieUtil.convertToString(jwtDto.getRefreshToken()));

    return ResponseMessage.responseHeadersEntity(AuthResponseCode.TOKEN_REISSUED, headers);
  }
}
