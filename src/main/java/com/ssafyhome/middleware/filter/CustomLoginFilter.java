package com.ssafyhome.middleware.filter;

import com.ssafyhome.model.dao.mapper.UserMapper;
import com.ssafyhome.model.dto.auth.JwtDto;
import com.ssafyhome.model.entity.mysql.UserEntity;
import com.ssafyhome.model.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
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
		response.setHeader("Authorization", "Bearer " + jwtDto.getAccessToken());
		response.addCookie(jwtDto.getRefreshToken());
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}
}
