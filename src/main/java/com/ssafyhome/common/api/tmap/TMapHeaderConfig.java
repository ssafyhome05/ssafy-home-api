package com.ssafyhome.common.api.tmap;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class TMapHeaderConfig {

	@Value("${t-map.API-KEY}")
	private String apiKey;

	@Bean
	public RequestInterceptor getRequestInterceptor() {

		return template -> {
			template.header("Content-Type", "application/json");
			template.header("Accept", "application/json");
			template.header("appKey", apiKey);
		};
	}
}
