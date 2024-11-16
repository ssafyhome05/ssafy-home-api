package com.ssafyhome.auth.filter;

import com.ssafyhome.auth.dao.RefreshTokenRepository;
import com.ssafyhome.auth.response.AuthResponseCode;
import com.ssafyhome.auth.service.JWTService;
import com.ssafyhome.common.response.ResponseMessage;
import com.ssafyhome.common.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {

	private final RefreshTokenRepository refreshTokenRepository;
	private final JWTService jwtService;
	private final CookieUtil cookieUtil;

	public CustomLogoutFilter(
			RefreshTokenRepository refreshTokenRepository,
			JWTService jwtService,
			CookieUtil cookieUtil
	) {

		this.refreshTokenRepository = refreshTokenRepository;
		this.jwtService = jwtService;
		this.cookieUtil = cookieUtil;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
	}

	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

		String requestURI = request.getRequestURI();
		String requestMethod = request.getMethod();
		if(!requestURI.startsWith("/auth/logout") || !requestMethod.equals("POST")) {
			filterChain.doFilter(request, response);
			return;
		}

		String refreshToken = jwtService.getRefreshToken(request);
		try {
			jwtService.checkRefreshTokenError(refreshToken);
			refreshTokenRepository.deleteById(refreshToken);
		} catch (Exception e) {}

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.SET_COOKIE, cookieUtil.convertToString(cookieUtil.deleteCookie("refresh")));

		ResponseMessage.builder()
				.responseCode(AuthResponseCode.LOGOUT_SUCCESS)
				.headers(headers)
				.build().setResponse(response);
	}
}
