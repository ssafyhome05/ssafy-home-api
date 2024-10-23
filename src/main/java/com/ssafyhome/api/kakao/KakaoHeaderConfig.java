package com.ssafyhome.api.kakao;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class KakaoHeaderConfig {

	@Value("${kakao.API-KEY}")
	private String apiKey;

	@Bean
	public RequestInterceptor getRequestInterceptor() {

		return template -> {
			template.header("Authorization", "KakaoAK " + apiKey);
		};
	}
}
