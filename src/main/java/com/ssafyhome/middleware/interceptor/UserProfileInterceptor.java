package com.ssafyhome.middleware.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafyhome.model.dao.mapper.UserMapper;
import com.ssafyhome.model.dto.user.UserProfileDto;
import com.ssafyhome.model.entity.mysql.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserProfileInterceptor implements HandlerInterceptor {

	private final UserMapper userMapper;

	public UserProfileInterceptor(UserMapper userMapper) {

		this.userMapper = userMapper;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication == null) return true;

		String role = authentication.getAuthorities().iterator().next().getAuthority();
		if (!role.equals("ROLE_USER")) return true;

		UserEntity userEntity = userMapper.getUserBySeq(authentication.getName());
		UserProfileDto userProfileDto = UserProfileDto.builder()
				.userSeq(userEntity.getUserSeq())
				.userId(userEntity.getUserId())
				.userName(userEntity.getUserName())
				.build();

		ObjectMapper objectMapper = new ObjectMapper();
		String userProfileJson = objectMapper.writeValueAsString(userProfileDto);
		response.setHeader("X-User-Profile", userProfileJson);

		return true;
	}
}
