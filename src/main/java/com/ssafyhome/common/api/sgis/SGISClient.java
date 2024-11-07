package com.ssafyhome.common.api.sgis;

import com.ssafyhome.common.api.sgis.dto.SgisAccessToken;
import com.ssafyhome.common.api.sgis.dto.SgisGeoCode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
