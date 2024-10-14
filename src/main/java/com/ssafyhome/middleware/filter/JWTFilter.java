package com.ssafyhome.middleware.filter;

import com.ssafyhome.exception.AccessTokenExpiredException;
import com.ssafyhome.exception.InvalidJwtException;
import com.ssafyhome.model.dao.mapper.UserMapper;
import com.ssafyhome.model.entity.mysql.UserEntity;
import com.ssafyhome.model.service.impl.CustomUserDetailsService;
import com.ssafyhome.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;
	private final UserMapper userMapper;
	private final CustomUserDetailsService userDetailsService;

	public JWTFilter(
			JWTUtil jwtUtil,
			UserMapper userMapper,
			CustomUserDetailsService userDetailsService
	) {

		this.jwtUtil = jwtUtil;
		this.userMapper = userMapper;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader == null) {

			filterChain.doFilter(request, response);
			return;
		}

		if (!authorizationHeader.startsWith("Bearer ")) {
			throw new InvalidJwtException("invalid JWT token");
		}

		String accessToken = authorizationHeader.substring(7);

		if (jwtUtil.isExpired(accessToken)) {
			throw new AccessTokenExpiredException("access token expired");
		}

		String category = jwtUtil.getKey(accessToken, "category");
		String userSeq = jwtUtil.getKey(accessToken, "userSeq");
		String userEmail = jwtUtil.getKey(accessToken, "userEmail");
		UserEntity userEntity = userMapper.getUserBySeqAndEmail(userSeq, userEmail);

		if (!category.equals("access") || userEntity == null) {
			throw new InvalidJwtException("invalid JWT token");
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(userEntity.getUserId());
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		filterChain.doFilter(request, response);
	}
}
