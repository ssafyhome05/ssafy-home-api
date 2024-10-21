package com.ssafyhome.api;

import com.ssafyhome.model.dto.TMapWalkRouteResponseDto;
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

	@PostMapping("/tmap/routes/pedestrain")
	TMapWalkRouteResponseDto findWalkRoute(
			@RequestHeader("Accept")
			String acceptType,

			@RequestHeader("Content-Type")
			String ContentType,

			@RequestHeader("appKey")
			String appKey,

			@RequestParam("version")
			int version,

			@RequestBody
			TMapWalkRouteRequestDto tMapWalkRouteRequestDto
	);

}
