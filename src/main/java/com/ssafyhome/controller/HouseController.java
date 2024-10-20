package com.ssafyhome.controller;

import com.ssafyhome.model.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(
		name = "House Controller",
		description = "부동산 매물 및 거래 내역 조회"
)
@RestController
@RequestMapping("/house")
public class HouseController {

	@Operation(
			summary = "",
			description = ""
	)

	@GetMapping("/deal/during")
	public ResponseEntity<List<HouseDealsDto>> getHouseDealsWithTimes(
			@RequestParam
			String houseSeq,

			@RequestParam
			String startDate,

			@RequestParam
			String endDate
	) {

		return null;
	}


	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/detail/status")
	public ResponseEntity<HouseGraphDto> getGraphInfo(
			@RequestParam
			String houseSeq
	) {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/deal")
	public ResponseEntity<List<HouseDealsDto>> getHouseDeals(
			@RequestParam
			String houseSeq
	) {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/detail")
	public ResponseEntity<HouseDetailDto> getHouseInfoDetail(
			@RequestParam
			String dongCode
	) {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/status")
	public ResponseEntity<LocationStatusDto> getHouseStatus(
			@RequestParam
			String dongCode
	) {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("")
	public ResponseEntity<List<HouseDto>> getHouseInfo(
			@RequestParam
			String dongCode
	) {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@PostMapping("/deal")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updateHouseDealsInfo() {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@PostMapping("")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updateHouseInfo() {

		return null;
	}
}
