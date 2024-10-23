package com.ssafyhome.api.kakao;

import com.ssafyhome.api.common.FeignSnakeCaseConfig;
import com.ssafyhome.model.dto.KakaoPlaceDto;
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

			@RequestParam("query")
			String query
	);

	@GetMapping("/v2/local/search/category.json")
	KakaoPlaceDto searchCategoryPlace(

			@RequestParam("category_group_code")
			String code
	);
}
