package com.ssafyhome.auth.dto;

import com.ssafyhome.auth.entity.AdminEntity;
import com.ssafyhome.user.entity.UserEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class CustomUserDetails implements UserDetails {

	private final UserEntity userEntity;
	private final AdminEntity adminEntity;
	private final String type;

	public CustomUserDetails(UserEntity userEntity) {

		this.userEntity = userEntity;
		this.adminEntity = null;
		this.type = "user";
	}

	public CustomUserDetails(AdminEntity adminEntity) {

		this.userEntity = null;
		this.adminEntity = adminEntity;
		this.type = "admin";
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(type.equals("user") ? "ROLE_USER" : "ROLE_ADMIN"));
		return authorities;
	}

	@Override
	public String getPassword() {
		return type.equals("user") ? userEntity.getUserPw() : adminEntity.toString();
	}

	@Override
	public String getUsername() {
		return type.equals("user") ? String.valueOf(userEntity.getUserSeq()) : adminEntity.toString();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
