package com.ssafyhome.house.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ssafyhome.common.mapper.GeometryMapper;
import com.ssafyhome.common.util.GeometryUtil;
import com.ssafyhome.common.util.object.Point;
import com.ssafyhome.house.dto.HouseDealDto;
import com.ssafyhome.house.dto.HouseDetailDto;
import com.ssafyhome.house.dto.HouseDto;
import com.ssafyhome.house.dto.HouseGraphDto;
import com.ssafyhome.house.service.HouseService;
import com.ssafyhome.spot.dto.LocationStatusDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


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
			summary = "설정된 기간 내 거래량 및 금액 반환",
			description = "startDatd 와 endDate 받아 조건에 맞는 List<HouseDealsDto> 반환"
	)
	@GetMapping("/deal/during")
	public ResponseEntity<List<HouseDealDto>> getHouseDealsWithTimes(
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
			summary = "매물별 연도 및 월별 시세 변동 그래프",
			description = "202208, 202209 등 특정 매물의 지금까지 월 별 거래 데이터 추이 <HouseGraphDto> 반환"
	)
	@GetMapping("/detail/status")
	public ResponseEntity<List<HouseGraphDto>> getGraphInfo(
			@RequestParam
			String houseSeq,
			@RequestParam
			int year
	) {

		return new ResponseEntity<>(houseService.getHouseGraph(houseSeq, year), HttpStatus.OK);
	}

	@Operation(
			summary = "매물별 모든 거래 내역 반환",
			description = "특정 매물의 거래금액 <HouseDealsDto>(page, limit)"
	)
	@GetMapping("/deal")
	public ResponseEntity<List<HouseDealDto>> getHouseDeals(
			@RequestParam
			String houseSeq,
			@RequestParam
			int page,
			@RequestParam
			int limit
	) {

		return new ResponseEntity<>(houseService.getHouseDealList(houseSeq, page, limit), HttpStatus.OK);
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
			@RequestParam("dongCode") 
			String dongCode,
			@RequestParam(value = "startDate", required = false) 
			String startDate,
			@RequestParam(value = "endDate", required = false) 
			String endDate,
			@RequestParam(value = "keyWord", required = false) String keyWord
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
	
	
	@Operation(
			summary = "행정동별 인구통계 데이터 업데이트",
			description = "행정동 시군구별 총인구수, 인구밀도, 노령화지수, 사업체 수, 총 주택수 정보 업데이트"
	)
	@GetMapping("/admin/register")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updatePopulationInfo() {

		String result = houseService.startPopulationTask();
		if (!result.equals("success")) {
			//excpeion 구현 필요
		}
		return new ResponseEntity<>("success", HttpStatus.CREATED);
	}
}
