package com.ssafyhome.api.kakao;

import com.ssafyhome.api.common.FeignSnakeCaseConfig;
import com.ssafyhome.model.dto.api.KakaoPlaceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
		name = "kakaoAPI",
		url = "https://dapi.kakao.com",
		configuration = {
				FeignSnakeCaseConfig.class,
				KakaoHeaderConfig.class
		}
)
public interface KakaoClient {

	@GetMapping("/v2/local/search/keyword.json")
	KakaoPlaceDto searchKeywordPlace(
			@RequestParam("query") String query,
			@RequestParam("x") double lng,
			@RequestParam("y") double lat,
			@RequestParam("radius") int radius,
			@RequestParam("page") int page
	);

	@GetMapping("/v2/local/search/category.json")
	KakaoPlaceDto searchCategoryPlace(
			@RequestParam("category_group_code") String code,
			@RequestParam("x") double lng,
			@RequestParam("y") double lat,
			@RequestParam("radius") int radius,
			@RequestParam("page") int page
	);
}

