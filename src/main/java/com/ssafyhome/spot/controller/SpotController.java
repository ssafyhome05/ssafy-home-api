package com.ssafyhome.spot.controller;

import com.ssafyhome.common.response.ResponseMessage;
import com.ssafyhome.spot.response.SpotResponseCode;
import com.ssafyhome.spot.service.SpotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
		name = "Spot Controller",
		description = "카테고리 별 장소 핸들링 (spot의 경우 지역 내 존재하는 편의시설을 의미합니다.) "
)
@RestController
@RequestMapping("/spot")
public class SpotController {

	private final SpotService spotService;

	public SpotController(SpotService spotService) {

		this.spotService = spotService;
	}

	@Operation(
			summary = "지도에 표시할 장소 정보",
			description = "dongCode 내 편의시설들의 카테고리별 List<SpotDto> 을 key:value 형태로 반환"
	)
	@GetMapping("")
	public ResponseEntity<ResponseMessage.CustomMessage> getSpotMap(
			@RequestParam
			String dongCode
	) {

		return ResponseMessage.responseDataEntity(SpotResponseCode.OK, spotService.getSpotsByDongCode(dongCode));
	}
}
