package com.ssafyhome.controller;

import com.ssafyhome.model.dto.NavigateDto;
import com.ssafyhome.model.dto.SpotSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
		name = "Navigate Controller",
		description = "부동산 매물과 각 장소간의 거리를 외부 API와 통신"
)
@RestController
@RequestMapping("/navigate")
public class NavigateController {

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

		return null;
	}

	@Operation(
			summary = "매물과 모든 사용자 장소간 이동시간",
			description = "houseSeq 와 등록한 모든 사용자장소 간 List<NavigateDto> 반환"
	)
	@GetMapping("/bookmark")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<NavigateDto>> getTimeWithCustomSpots(
			@RequestParam
			String houseSeq
	) {

		return null;
	}

	@Operation(
			summary = "각 장소 카테고리별 가장 가까운 곳의 소요시간",
			description = "다이소 및 편의점(편의시설), 지하철(교통시설), 맛집(상업시설) 별 최소소요거리"
	)
	@GetMapping("/spot")
	public ResponseEntity<List<NavigateDto>> getTimeWithSpots(
			@RequestParam
			String houseSeq
	) {

		return null;
	}
}
