package com.ssafyhome.auth.filter;

import com.ssafyhome.auth.response.AuthResponseCode;
import com.ssafyhome.common.response.ResponseMessage;
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

			ResponseMessage.setBasicResponse(response,AuthResponseCode.INVALID_JWT_TOKEN);
			return;
		}

		String accessToken = authorizationHeader.substring(7);

		try {
			jwtUtil.isExpired(accessToken);
		} catch (ExpiredJwtException e) {

			ResponseMessage.setBasicResponse(response, AuthResponseCode.ACCESS_TOKEN_EXPIRED);
			return;
		} catch (SignatureException e) {

			ResponseMessage.setBasicResponse(response, AuthResponseCode.INVALID_JWT_TOKEN);
			return;
		}

		UserDetails userDetails = null;

		String category = jwtUtil.getKey(accessToken, "category");
		String userSeq = jwtUtil.getKey(accessToken, "userSeq");
		if (userSeq != null) {

			String userEmail = jwtUtil.getKey(accessToken, "userEmail");

			UserEntity userEntity = userMapper.getUserBySeqAndEmail(userSeq, userEmail);

			if (!category.equals("access") || userEntity == null) {

				ResponseMessage.setBasicResponse(response, AuthResponseCode.INVALID_JWT_TOKEN);
				return;
			}
			userDetails = userDetailsService.loadUserByUsername(userEntity.getUserId());
		}
		else {

			String adminSeq = jwtUtil.getKey(accessToken, "adminSeq");
			String role = jwtUtil.getKey(accessToken, "role");

			if (!category.equals("access")) {

				ResponseMessage.setBasicResponse(response, AuthResponseCode.INVALID_JWT_TOKEN);
				return;
			}
			userDetails = userDetailsService.loadAdminByUsername(adminSeq, role);
		}

		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		filterChain.doFilter(request, response);
	}
}
