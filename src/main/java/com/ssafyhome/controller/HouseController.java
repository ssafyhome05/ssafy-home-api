package com.ssafyhome.controller;

import com.ssafyhome.model.dao.mapper.GeometryMapper;
import com.ssafyhome.model.dto.house.HouseDealsDto;
import com.ssafyhome.model.dto.house.HouseDetailDto;
import com.ssafyhome.model.dto.house.HouseDto;
import com.ssafyhome.model.dto.house.HouseGraphDto;
import com.ssafyhome.model.dto.spot.LocationStatusDto;
import com.ssafyhome.model.service.HouseService;
import com.ssafyhome.util.GeometryUtil;
import com.ssafyhome.util.calc.Point;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Tag(
		name = "House Controller",
		description = "부동산 매물 및 거래 내역 조회"
)
@RestController
@RequestMapping("/house")
public class HouseController {

	private final HouseService houseService;
	private final GeometryUtil geometryUtil;
	private final GeometryMapper geometryMapper;

	public HouseController(HouseService houseService, GeometryUtil geometryUtil, GeometryMapper geometryMapper) {

		this.houseService = houseService;
		this.geometryUtil = geometryUtil;
		this.geometryMapper = geometryMapper;
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
			String dongCode,
			@RequestParam(required = false)
			String startDate,
			@RequestParam(required = false)
			String endDate,
			@RequestParam(required = false)
			String keyWord
	) {
		Map<String, Object> params = new HashMap<>();
		params.put("dongCode", dongCode);

		if (startDate != null && !startDate.isEmpty()) {
			String[] start = startDate.split("-");
			params.put("startYear", start[0]);
			params.put("startMonth", start[1]);
		}else{
			params.put("startYear", "");
			params.put("startMonth", "");
		}

		if (endDate != null && !endDate.isEmpty()) {
			String[] end = endDate.split("-");
			params.put("endYear", end[0]);
			params.put("endMonth", end[1]);
		} else{
			params.put("endYear", "");
			params.put("endMonth", "");
		}
		params.put("keyWord", keyWord);

		return new ResponseEntity<>(houseService.getHouseInfo(params), HttpStatus.OK);
//		return new ResponseEntity<>(houseService.getHouseInfo(dongCode), HttpStatus.OK);
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/polygon")
	public List<Point> getDongPolygon(
			@RequestParam
			String dongCode
	){
		List<Point> dongPolygonList = geometryUtil.getPoints(geometryMapper.selectByDongCode(dongCode));
		return dongPolygonList;
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

		return houseService.getSseEmitter(requestId);
	}
}
