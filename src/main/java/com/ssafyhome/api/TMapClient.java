package com.ssafyhome.api;

import com.ssafyhome.config.FeignConfig;
import com.ssafyhome.model.dto.TMapWalkRouteRequestDto;
import com.ssafyhome.model.dto.TMapWalkRouteResponseDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
		name = "t-mapAPI",
		url = "https://apis.openapi.sk.com"
)
public interface TMapClient {

	@PostMapping("/tmap/routes/pedestrian")
	@Headers({
			"Accept: application/json",
			"Content-Type: application/json",
	})
	TMapWalkRouteResponseDto findWalkRoute(
			@RequestHeader("appKey")
			String appKey,

			@RequestParam("version")
			int version,

			@RequestBody
			TMapWalkRouteRequestDto tMapWalkRouteRequestDto
	);
}
