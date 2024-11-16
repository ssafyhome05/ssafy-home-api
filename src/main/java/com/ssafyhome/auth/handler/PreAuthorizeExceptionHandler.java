package com.ssafyhome.auth.handler;

import com.ssafyhome.auth.response.AuthResponseCode;
import com.ssafyhome.common.response.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;

@Component
public class PreAuthorizeExceptionHandler {

	/**
	 * @PreAuthorize의 경우 Filter단에서 권한을 처리하므로 ControllerAdvice를 통한 Handling에 어려움이 있음
	 * 그래서 따로 인증 및 권한 오류를 핸들링 하기 위해 빈으로 등록
	 * - customAccessDeniedHandler - 403 Forbidden
	 * - customAuthenticationEntryPoint - 401 Unauthorized
	 */
	@Bean
	public AccessDeniedHandler customAccessDeniedHandler() {

		return (request, response, exception) -> ResponseMessage.setDataResponse(response, AuthResponseCode.METHOD_FORBIDDEN, getErrorDetails(request, exception));
	}

	@Bean
	public AuthenticationEntryPoint customAuthenticationEntryPoint() {

		return (request, response, exception) -> ResponseMessage.setDataResponse(response, AuthResponseCode.METHOD_UNAUTHORIZED, getErrorDetails(request, exception));
	}

	public Map<String, Object> getErrorDetails(HttpServletRequest request, Exception exception) {

		String methodName;
		String requiredAuthorities = "Unknown";

		try {
			HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
			Method method = handlerMethod.getMethod();
			methodName = method.getDeclaringClass().getSimpleName() + "#" + method.getName();

			PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);
			if (preAuthorize != null) requiredAuthorities = preAuthorize.value();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentAuthorities = "No authorities";
		if (authentication != null) {
			currentAuthorities = authentication.getAuthorities().iterator().next().getAuthority();
		}

		return Map.of(
				"method", methodName,
				"requiredAuthorities", requiredAuthorities,
				"currentAuthorities", currentAuthorities
		);
	}
}
