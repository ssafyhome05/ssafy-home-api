package com.ssafyhome.model.service.impl;

import com.ssafyhome.model.dao.mapper.UserMapper;
import com.ssafyhome.model.dto.CustomUserDetails;
import com.ssafyhome.model.entity.mysql.UserEntity;
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
}
