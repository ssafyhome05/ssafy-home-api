package com.ssafyhome.spot.controller;

import com.ssafyhome.spot.dto.SpotDto;
import com.ssafyhome.spot.service.SpotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(
		name = "Spot Controller",
		description = "카테고리 별 장소 핸들링"
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
			description = "dongCode 와 일치하는 List<SpotDto> 반환"
	)
	@GetMapping("")
	public ResponseEntity<Map<String,List<SpotDto>>> getSpots(
			@RequestParam
			String dongCode
	) {

		return new ResponseEntity<>(spotService.getSpotsByDongCode(dongCode), HttpStatus.OK);
	}

	@Operation(
			summary = "Spot 등록",
			description = "SpotDto 객체 등록"
	)
	@PostMapping("")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updateSpotInfo() {

		return null;
	}
}
