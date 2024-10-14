package com.ssafyhome.handler;

import com.ssafyhome.model.dto.CustomOAuth2User;
import com.ssafyhome.model.dto.JwtDto;
import com.ssafyhome.model.service.JWTService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

	@Value("${front-end.url}")
	private String frontEndUrl;

	private final JWTService jwtService;

	public CustomOAuth2SuccessHandler(JWTService jwtService) {

		this.jwtService = jwtService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
		String userSeq = customOAuth2User.getName();
		String userEmail = customOAuth2User.getEmail();
		JwtDto jwtDto = jwtService.setTokens(userSeq, userEmail);
		response.setHeader("Authorization", "Bearer " + jwtDto.getAccessToken());
		response.addCookie(jwtDto.getRefreshToken());
	}
}
