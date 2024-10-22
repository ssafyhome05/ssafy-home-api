package com.ssafyhome.api;

import com.ssafyhome.config.FeignSnakeCaseConfig;
import com.ssafyhome.model.dto.KakaoKeywordPlaceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
		name = "kakaoAPI",
		url = "https://dapi.kakao.com",
		configuration = FeignSnakeCaseConfig.class
)
public interface KakaoClient {

	@GetMapping("/v2/local/search/keyword.json")
	KakaoKeywordPlaceDto searchKeywordPlace(
			@RequestHeader("Authorization")
			String apiKey,

			@RequestParam("query")
			String query
	);
}
