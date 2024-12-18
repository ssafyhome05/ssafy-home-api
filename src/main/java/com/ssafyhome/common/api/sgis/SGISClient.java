package com.ssafyhome.common.api.sgis;

import com.ssafyhome.common.api.sgis.dto.SgisSearchPopulation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssafyhome.common.api.sgis.dto.SgisAccessToken;
import com.ssafyhome.common.api.sgis.dto.SgisGeoCode;
import com.ssafyhome.common.api.sgis.dto.SgisPopulation;

@FeignClient(
		name = "SGISAPI",
		url = "https://sgisapi.kostat.go.kr",
		configuration = FeignSnakeCaseConfig.class
)
public interface SGISClient {

	@GetMapping("/OpenAPI3/auth/authentication.json")
	SgisAccessToken getAccessToken(

			@RequestParam("consumer_key")
			String consumerKey,

			@RequestParam("consumer_secret")
			String consumerSecret
	);
	

	@GetMapping("/OpenAPI3/addr/geocodewgs84.json")
	SgisGeoCode getGeocode(

			@RequestParam("accessToken")
			String accessToken,

			@RequestParam("address")
			String address
	);
	// errCD - 401인 경우 accessTokne 만료 -> getAccessToken 재실행
	// errCnt 선언해서 10회 이상 실패시 에러 throw 필요
	
	@GetMapping("/OpenAPI3/stats/population.json")
	SgisPopulation getPopulation(
			
			@RequestParam("accessToken")
			String accessToken,
			
			@RequestParam("year")
			int year,
			
			@RequestParam("adm_cd")
			int admCd,
			// 행정동코드를 받아서 반환

			
			@RequestParam(value = "low_search")
			int lowSearch
	);

	@GetMapping("/OpenAPI3/stats/searchpopulation.json")
	SgisSearchPopulation getSearchPopulation(

			@RequestParam("accessToken")
			String accessToken,

			@RequestParam("year")
			int year,

			@RequestParam("adm_cd")
			int admCd,

			@RequestParam("age_type")
			String ageType,

			@RequestParam(value = "low_search")
			int lowSearch
	);
}
