package com.ssafyhome.auth.filter;

import com.ssafyhome.auth.response.AuthResponseCode;
import com.ssafyhome.common.response.ResponseMessage;
import com.ssafyhome.user.dao.UserMapper;
import com.ssafyhome.auth.dto.JwtDto;
import com.ssafyhome.user.entity.UserEntity;
import com.ssafyhome.auth.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JWTService jwtService;
	private final UserMapper userMapper;

	public CustomLoginFilter(
			AuthenticationManager authenticationManager,
			JWTService jwtService,
			UserMapper userMapper
	) {

		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.userMapper = userMapper;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		String userId = obtainUsername(request);
		String password = obtainPassword(request);

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(userId, password);

		return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

		String userId = authResult.getName();
		UserEntity userEntity = userMapper.getUserBySeq(userId);
		String userSeq = String.valueOf(userEntity.getUserSeq());
		String userEmail = userEntity.getUserEmail();
		JwtDto jwtDto = jwtService.setTokens(userSeq, userEmail);

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtDto.getAccessToken());
		headers.add(HttpHeaders.SET_COOKIE, jwtDto.getAccessToken());

		ResponseMessage.setHeadersResponse(response, AuthResponseCode.LOGIN_SUCCESS, headers);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

		ResponseMessage.setBasicResponse(response, AuthResponseCode.FAIL_TO_LOGIN);
	}
}
