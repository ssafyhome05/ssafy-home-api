package com.ssafyhome.controller;

import com.ssafyhome.model.dto.SpotDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
		name = "Spot Controller",
		description = "카테고리 별 장소 핸들링"
)
@RestController
@RequestMapping("/spot")
public class SpotController {

	@Operation(
			summary = "지도에 표시할 장소 정보",
			description = "dongCode 와 일치하는 List<SpotDto> 반환"
	)
	@GetMapping("/")
	public ResponseEntity<List<SpotDto>> getSpots(
			@RequestParam
			String dongCode
	) {

		return null;
	}

	@Operation(
			summary = "Spot 등록",
			description = "SpotDto 객체 등록"
	)
	@PostMapping("/")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updateSpotInfo() {

		return null;
	}
}
