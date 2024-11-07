package com.ssafyhome.navigate.controller;

import com.ssafyhome.navigate.dto.NavigateDto;
import com.ssafyhome.spot.dto.SpotSearchDto;
import com.ssafyhome.navigate.service.NavigateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
		name = "Navigate Controller",
		description = "부동산 매물과 각 장소간의 거리를 외부 API와 통신"
)
@RestController
@RequestMapping("/navigate")
public class NavigateController {

	private final NavigateService navigateService;

	public NavigateController(NavigateService navigateService) {

		this.navigateService = navigateService;
	}

	@Operation(
			summary = "매물 과 검색한 장소 간 이동시간",
			description = "houseSeq 의 장소와 spotSearchDto 장소 간 NavigateDto 반환"
	)
	@GetMapping("/search")
	public ResponseEntity<NavigateDto> getTimeWithSearchSpot(
			@RequestParam
			String houseSeq,

			@RequestBody
			SpotSearchDto spotSearchDto
	) {

		return new ResponseEntity<>(navigateService.getNavigate("search", houseSeq, navigateService.getEndPoint(spotSearchDto)), HttpStatus.OK);
	}

	@Operation(
			summary = "매물과 모든 장소간 이동시간 및 경로 (사용자 정의 장소 / 카테고리별 최단거리)",
			description = "houseSeq 와 모든 장소간 List<NavigateDto> 반환"
	)
	@GetMapping("/{type}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<NavigateDto>> getTimeWithCustomSpots(
			@PathVariable
			String type,

			@RequestParam
			String houseSeq
	) {

		return new ResponseEntity<>(navigateService.getNavigates(type, houseSeq), HttpStatus.OK);
	}
}
