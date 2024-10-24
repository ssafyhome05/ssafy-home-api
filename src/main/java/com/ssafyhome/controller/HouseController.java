package com.ssafyhome.controller;

import com.ssafyhome.model.dto.house.HouseDealsDto;
import com.ssafyhome.model.dto.house.HouseDetailDto;
import com.ssafyhome.model.dto.house.HouseDto;
import com.ssafyhome.model.dto.house.HouseGraphDto;
import com.ssafyhome.model.dto.spot.LocationStatusDto;
import com.ssafyhome.model.service.HouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;


@Tag(
		name = "House Controller",
		description = "부동산 매물 및 거래 내역 조회"
)
@RestController
@RequestMapping("/house")
public class HouseController {

	private final HouseService houseService;

	public HouseController(HouseService houseService) {

		this.houseService = houseService;
	}

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
			String houseSeq,
			@RequestParam
			int page,
			@RequestParam
			int limit
	) {

		return new ResponseEntity<>(houseService.getHouseDeals(houseSeq, page, limit), HttpStatus.OK);
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

		return new ResponseEntity<>(houseService.getHouseInfo(dongCode), HttpStatus.OK);
	}

	@Operation(
			summary = "",
			description = ""
	)
	@PostMapping("/admin/register")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updateHouseInfo(
			@RequestParam(required = false, defaultValue = "11110")
			int startCd,

			@RequestParam(required = false, defaultValue = "60000")
			int endCd,

			@RequestParam
			int dealYmd
	) {

		String requestId = houseService.startHouseInfoTask(dealYmd, startCd, endCd);
		return new ResponseEntity<>(requestId, HttpStatus.CREATED);
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/task/{requestId}/status")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public SseEmitter subscribeHouseInfoProcess(
			@PathVariable
			String requestId
	) {

		return houseService.getHouseInfoTask(requestId);
	}
}
