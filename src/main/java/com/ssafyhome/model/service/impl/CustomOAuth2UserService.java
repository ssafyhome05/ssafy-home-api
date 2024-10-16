package com.ssafyhome.model.service.impl;

import com.ssafyhome.model.dao.mapper.UserMapper;
import com.ssafyhome.model.dto.AdminOAuth2User;
import com.ssafyhome.model.dto.CustomOAuth2User;
import com.ssafyhome.model.entity.mysql.UserEntity;
import com.ssafyhome.model.dto.oauth2response.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	
	private final UserMapper userMapper;
	private final BCryptPasswordEncoder passwordEncoder;
	
	public CustomOAuth2UserService(
			UserMapper userMapper,
			BCryptPasswordEncoder passwordEncoder
	) {
		
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = switch (registrationId) {
			case "google" -> new GoogleResponse(oAuth2User.getAttributes());
			case "naver" -> new NaverResponse(oAuth2User.getAttributes());
			case "kakao" -> new KakaoResponse(oAuth2User.getAttributes());
			case "admin" -> new AdminResponse(oAuth2User.getAttributes());
			default -> null;
		};

		if (oAuth2Response == null) return null;
		if (oAuth2Response instanceof AdminResponse) {
			return new AdminOAuth2User((AdminResponse) oAuth2Response);
		}
		else {
			return handleUser(oAuth2Response);
		}
	}

	private OAuth2User handleUser(OAuth2Response oAuth2Response) {

		String userId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
		UserEntity existUser = userMapper.getUserById(userId);

		if (existUser == null) {

			UserEntity userEntity = new UserEntity();
			userEntity.setUserId(userId);
			userEntity.setUserName(oAuth2Response.getName());
			userEntity.setUserPw(passwordEncoder.encode(userId));
			userEntity.setUserEmail(oAuth2Response.getEmail());
			try {
				userMapper.insertUser(userEntity);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			existUser = userEntity;
		}
		else {
			existUser.setUserEmail(oAuth2Response.getEmail());
			userMapper.updateUser(existUser);
		}
		return new CustomOAuth2User(existUser);
	}
}
