package com.ssafyhome.auth.service;

import com.ssafyhome.auth.entity.AdminEntity;
import com.ssafyhome.user.dao.UserMapper;
import com.ssafyhome.auth.dto.CustomUserDetails;
import com.ssafyhome.user.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserMapper userMapper;

	public CustomUserDetailsService(UserMapper userMapper) {

		this.userMapper = userMapper;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserEntity userEntity = userMapper.getUserById(username);
		if (userEntity == null) return null;
		return new CustomUserDetails(userEntity);
	}

	public UserDetails loadAdminByUsername(String username, String role) throws UsernameNotFoundException {

		AdminEntity adminEntity = new AdminEntity(username, role);
		return new CustomUserDetails(adminEntity);
	}
}
