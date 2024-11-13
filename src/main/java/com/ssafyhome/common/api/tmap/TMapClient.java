package com.ssafyhome.common.api.tmap;

import com.ssafyhome.common.api.tmap.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
		name = "t-mapAPI",
		url = "https://apis.openapi.sk.com",
		configuration = TMapHeaderConfig.class
)
public interface TMapClient {

	@PostMapping("/tmap/routes")
	TMapCarRouteResponseDto findCarRoute(

			@RequestParam("version")
			int version,

			@RequestBody
			TMapCarRouteRequestDto tMapCarRouteRequestDto
	);

	@PostMapping("/tmap/routes/pedestrian")
	TMapWalkRouteResponseDto findWalkRoute(

			@RequestParam("version")
			int version,

			@RequestBody
			TMapWalkRouteRequestDto tMapWalkRouteRequestDto
	);

	@PostMapping("/transit/routes")
	TMapTransportRouteResponseDto findTransportRoute(@RequestBody TMapTransportRouteRequestDto tMapTransportRouteRequestDto);
}
