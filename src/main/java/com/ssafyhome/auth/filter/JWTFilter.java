package com.ssafyhome.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafyhome.user.dao.UserMapper;
import com.ssafyhome.user.entity.UserEntity;
import com.ssafyhome.auth.service.CustomUserDetailsService;
import com.ssafyhome.common.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String accessToken = authorizationHeader.substring(7);

		try {
			jwtUtil.isExpired(accessToken);
		} catch (ExpiredJwtException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			Map<String, Object> errorDetails = new HashMap<>();
			errorDetails.put("message", "Token has expired");
			errorDetails.put("status", HttpServletResponse.SC_UNAUTHORIZED);

			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getWriter(), errorDetails);

			return;
		} catch (SignatureException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}



		String category = jwtUtil.getKey(accessToken, "category");
		String userSeq = jwtUtil.getKey(accessToken, "userSeq");
		String userEmail = jwtUtil.getKey(accessToken, "userEmail");
		UserEntity userEntity = userMapper.getUserBySeqAndEmail(userSeq, userEmail);

		if (!category.equals("access") || userEntity == null) {

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(userEntity.getUserId());
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		filterChain.doFilter(request, response);
	}
}
