package com.ssafyhome.auth.service;

import com.ssafyhome.auth.dto.oauth2response.*;
import com.ssafyhome.user.dao.UserMapper;
import com.ssafyhome.auth.dto.AdminOAuth2User;
import com.ssafyhome.auth.dto.CustomOAuth2User;
import com.ssafyhome.user.entity.UserEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	
	private final UserMapper userMapper;
	
	public CustomOAuth2UserService(
			UserMapper userMapper
	) {
		
		this.userMapper = userMapper;
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

			UserEntity userEntity = UserEntity.builder()
					.userId(userId)
					.userName(oAuth2Response.getName())
					.userEmail(oAuth2Response.getEmail())
					.socialPlatform(oAuth2Response.getProvider())
					.build();

			try {
				userMapper.insertOAuth2User(userEntity);
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
