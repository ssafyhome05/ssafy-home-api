package com.ssafyhome.house.controller;

import com.ssafyhome.common.mapper.GeometryMapper;
import com.ssafyhome.common.response.ResponseMessage;
import com.ssafyhome.common.util.GeometryUtil;
import com.ssafyhome.common.util.object.Point;
import com.ssafyhome.house.dto.HouseSearchWithTimeDto;
import com.ssafyhome.house.response.HouseResoponseCode;
import com.ssafyhome.house.service.HouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
			summary = "설정된 기간 내 거래량 및 금액 반환",
			description = "startDatd 와 endDate 받아 조건에 맞는 List<HouseDealsDto> 반환"
	)
	@GetMapping("/deal/during")
	public ResponseEntity<ResponseMessage.CustomMessage> getHouseDealsWithTimes(
			@RequestParam()
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
	public ResponseEntity<ResponseMessage.CustomMessage> getGraphInfo(
			@Parameter(
			          name = "houseSeq"
			      )
			@RequestParam("houseSeq")
			String houseSeq,
			@Parameter(
			          name = "year"
			      )
			@RequestParam("year")
			int year
	) {

		return ResponseMessage.responseDataEntity(HouseResoponseCode.OK, houseService.getHouseGraph(houseSeq, year));
	}

	@Operation(
			summary = "매물별 모든 거래 내역 반환",
			description = "특정 매물의 거래금액 <HouseDealsDto>(page, limit)"
	)
	@GetMapping("/deal")
	public ResponseEntity<ResponseMessage.CustomMessage> getHouseDeals(
			@RequestParam("houseSeq")
			String houseSeq,
			@RequestParam("page")
			int page,
			@RequestParam("limit")
			int limit
	) {

		return ResponseMessage.responseDataEntity(HouseResoponseCode.OK, houseService.getHouseDealList(houseSeq, page, limit));
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/detail")
	public ResponseEntity<ResponseMessage.CustomMessage> getHouseInfoDetail(
			@Parameter(
			          name = "dongCode"
			      )
			@RequestParam("dongcode")
			String dongCode
	) {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/status")
	public ResponseEntity<ResponseMessage.CustomMessage> getHouseStatus(
			@Parameter(
			          name = "dongCode"
			      )
			@RequestParam("dongcode")
			String dongCode
	) {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("")
	public ResponseEntity<ResponseMessage.CustomMessage> getHouseInfo(
			@RequestParam
			HouseSearchWithTimeDto searchDto
	) {

		return ResponseMessage.responseDataEntity(HouseResoponseCode.OK, houseService.getHouseInfo(searchDto));
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/polygon")
	public ResponseEntity<ResponseMessage.CustomMessage> getDongPolygon(
			@RequestParam("dongCode")
			@Parameter(
			          name = "dongCode"
			      )
			String dongCode
	){
		List<Point> dongPolygonList = geometryUtil.getPoints(geometryMapper.selectByDongCode(dongCode));
		return ResponseMessage.responseDataEntity(HouseResoponseCode.OK, dongPolygonList);
	}

	@Operation(
			summary = "",
			description = ""
	)
	@PostMapping("/admin/register")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ResponseMessage.CustomMessage> updateHouseInfo(
			@RequestParam(required = false, defaultValue = "11110")
			int startCd,

			@RequestParam(required = false, defaultValue = "60000")
			int endCd,

			@RequestParam
			int dealYmd
	) {

		String requestId = houseService.startHouseInfoTask(dealYmd, startCd, endCd);
		String taskURI = "http://localhost:8080/api/house/task/" + requestId + "/status";
		return ResponseMessage.responseDataEntity(HouseResoponseCode.TASK_STATUS_CREATED, taskURI);
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/task/{requestId}/status")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public SseEmitter subscribeHouseInfoProcess(
			@Parameter(
			          name = "requestId"
			      )
			@PathVariable("requestId")
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
	public ResponseEntity<ResponseMessage.CustomMessage> updatePopulationInfo() {

		houseService.startPopulationTask("2022");
		return ResponseMessage.responseBasicEntity(HouseResoponseCode.POPULATION_UPDATED);
	}

	@PostMapping("/search")
	public ResponseEntity<ResponseMessage.CustomMessage> inputSearchKeyword(
			@RequestBody
			String dongCode
	) {

		houseService.saveSearchKeyword(dongCode);
		return ResponseMessage.responseBasicEntity(HouseResoponseCode.KEYWORD_SUCCESS_SAVED);
	}

	@GetMapping("/top-ten")
	public ResponseEntity<ResponseMessage.CustomMessage> getTopTen() {

		return ResponseMessage.responseDataEntity(HouseResoponseCode.OK, houseService.getTopTen());
	}
}
