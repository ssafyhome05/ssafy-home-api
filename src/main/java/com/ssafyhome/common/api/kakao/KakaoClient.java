package com.ssafyhome.common.api.kakao;

import com.ssafyhome.common.api.sgis.FeignSnakeCaseConfig;
import com.ssafyhome.common.api.kakao.dto.KakaoPlaceDto;
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

	@GetMapping("/v2/local/search/keyword.json")
	KakaoPlaceDto searchKeywordPlace(
			@RequestParam("query") String query,
			@RequestParam("x") double lng,
			@RequestParam("y") double lat,
			@RequestParam("radius") int radius,
			@RequestParam("size") int size,
			@RequestParam("sort") String sort
	);

	@GetMapping("/v2/local/search/category.json")
	KakaoPlaceDto searchCategoryPlace(
			@RequestParam("category_group_code") String code,
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
			@RequestParam("size") int size,
			@RequestParam("sort") String sort
	);
}

