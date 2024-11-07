package com.ssafyhome.auth.handler;

import com.ssafyhome.auth.dto.AdminOAuth2User;
import com.ssafyhome.auth.dto.CustomOAuth2User;
import com.ssafyhome.auth.dto.JwtDto;
import com.ssafyhome.auth.service.JWTService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
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

		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		if (oAuth2User instanceof CustomOAuth2User) {
			onAuthenticationUserSuccess(response, (CustomOAuth2User) oAuth2User);
		} else {
			onAuthenticationAdminSuccess(request, response, (AdminOAuth2User) oAuth2User);
		}
	}

	private void onAuthenticationUserSuccess(HttpServletResponse response, CustomOAuth2User customOAuth2User) throws IOException, ServletException {

		String userSeq = String.valueOf(customOAuth2User.getSeq());
		String userEmail = customOAuth2User.getEmail();
		JwtDto jwtDto = jwtService.setTokens(userSeq, userEmail);
		response.setHeader("Authorization", "Bearer " + jwtDto.getAccessToken());
		response.addCookie(jwtDto.getRefreshToken());
	}

	private void onAuthenticationAdminSuccess(HttpServletRequest request, HttpServletResponse response, AdminOAuth2User adminOAuth2User) {

		Authentication authentication = new OAuth2AuthenticationToken(
				adminOAuth2User,
				adminOAuth2User.getAuthorities(),
				"admin"
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		SecurityContextRepository repository = new HttpSessionSecurityContextRepository();
		repository.saveContext(SecurityContextHolder.getContext(), request, response);
	}
}
