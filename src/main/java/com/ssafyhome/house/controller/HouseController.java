package com.ssafyhome.house.controller;

import com.ssafyhome.common.response.ResponseMessage;

import com.ssafyhome.house.dto.HouseSearchWithTimeDto;
import com.ssafyhome.house.code.HouseResoponseCode;

import com.ssafyhome.house.service.HouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(
		name = "House Controller",
		description = "부동산 매물 및 거래 내역 조회"
)
@RestController
@RequestMapping("/house")
public class HouseController {

	/**
	 * DI
	 */
	private final HouseService houseService;

	public HouseController(HouseService houseService) {

		this.houseService = houseService;
	}

	/**
	 * GET MAPPING
	 */
	@Operation(
			summary = "인기 검색지역 상위 10개 반환",
			description = "/api/house/search 를 통해 등록된 검색 지역들을 계산하여 상위 10개 지역 반환"
	)
	@GetMapping("/topten")
	public ResponseEntity<ResponseMessage.CustomMessage> getTopTen() {

		return ResponseMessage.responseDataEntity(
				HouseResoponseCode.OK,
				houseService.getTopTen()
		);
	}

	@Operation(
			summary = "새로운 매물 데이터 업데이트 진척도 확인 (관리자)",
			description = "신규 매물 거래 내역의 업데이트 진척도를 반환합니다. (관리자전용)"
	)
	@GetMapping("/task/{requestId}/status")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public SseEmitter subscribeHouseInfoProcess(

			@Parameter(name = "SSE Task Id")
			@PathVariable("requestId")
			String requestId
	) {

		return houseService.getSseEmitter(requestId);
	}

	@Operation(
			summary = "특정 연도의 특정 매물의 월별 시세 변동 그래프",
			description = "특정 매물의 지금까지 월 별 거래 데이터 추이를 반환합니다."
	)
	@GetMapping("/status")
	public ResponseEntity<ResponseMessage.CustomMessage> getGraphInfo(

			@Parameter(name = "매물 번호")
			@RequestParam("houseSeq")
			String houseSeq,

			@Parameter(name = "그래프 기준 연도")
			@RequestParam("year")
			int year
	) {

		return ResponseMessage.responseDataEntity(
				HouseResoponseCode.OK,
				houseService.getHouseGraph(houseSeq, year)
		);
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

		return ResponseMessage.responseDataEntity(
				HouseResoponseCode.OK,
				houseService.getHouseDealList(houseSeq, page, limit)
		);
	}

	@Operation(
			summary = "동코드별 인구통계 반환",
			description = "지역별 총인구, 가구총계, 노령화지수, 평균연령을 반환합니다."
	)
	@GetMapping("/population")
	public ResponseEntity<ResponseMessage.CustomMessage> getPopulation(

			@Parameter(
					name = "dongCode",
					example = "1111010100"
			)
			@RequestParam("dongCode")
			String dongCode
	) {

		return ResponseMessage.responseDataEntity(
				HouseResoponseCode.OK,
				houseService.getPopulation(dongCode)
		);
	}

	@Operation(
			summary = "지도 상 법정동 경계선 반환",
			description = "검색한 법정동의 경계면 좌표 리스트를 반환합니다."
	)
	@GetMapping("/polygon")
	public ResponseEntity<ResponseMessage.CustomMessage> getDongPolygon(

			@RequestParam("dongCode")
			@Parameter(
					name = "dongCode",
					example = "1111010100"
			)
			String dongCode
	){

		return ResponseMessage.responseDataEntity(
				HouseResoponseCode.OK,
				houseService.getPoints(dongCode)
		);
	}

	@Operation(
			summary = "최신 뉴스기사 크롤링",
			description = "네이버부동산 기준 최신 뉴스기사 제목, 링크, 관련동네, 출처를 가져옵니다."
	)
	@GetMapping("/news")
	public ResponseEntity<ResponseMessage.CustomMessage> getNewsList() {

		return ResponseMessage.responseDataEntity(
				HouseResoponseCode.OK,
				houseService.getNewsList()
		);
	}

	
	
	
	
	/**
	 * POST MAPPING
	 */
	@Operation(
			summary = "특정 지역의 매물 존재정보 반환",
			description = "사용자가 입력한 dongcode(필수) 와 세부사항(연도, 매물이름) 을 기준으로 매물 존재내역을 반환합니다."
	)
	@PostMapping("")
	public ResponseEntity<ResponseMessage.CustomMessage> getHouseInfo(

			@RequestBody
			HouseSearchWithTimeDto searchDto
	) {

		return ResponseMessage.responseDataEntity(
				HouseResoponseCode.OK,
				houseService.getHouseInfo(searchDto)
		);
	}

	@Operation(
			summary = "지역 검색 시 검색 지역 수집",
			description = "지역을 검색하면 해당 지역의 코드를 Redis에 저장하여 인기 검색 지역에 반영"
	)
	@PostMapping("/search")
	public ResponseEntity<ResponseMessage.CustomMessage> inputSearchKeyword(
			@RequestParam
			String dongCode
	) {

		houseService.saveSearchKeyword(dongCode);
		return ResponseMessage.responseBasicEntity(
				HouseResoponseCode.KEYWORD_SUCCESS_SAVED
		);
	}

	@Operation(
			summary = "새로운 매물 데이터 업데이트 (관리자) ",
			description = "신규 매물 거래 내역을 업데이트 합니다. (관리자) "
	)
	@PostMapping("/admin/house")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ResponseMessage.CustomMessage> updateHouseInfo(
			@RequestParam(required = false, defaultValue = "11110")
			int startCd,

			@RequestParam(name="endCd", required = false, defaultValue = "60000")
			int endCd,

			@RequestParam(name="dealYmd")
			int dealYmd
	) {

		return ResponseMessage.responseDataEntity(
				HouseResoponseCode.TASK_STATUS_CREATED,
				houseService.startHouseInfoTask(dealYmd, startCd, endCd)
		);
	}

	@Operation(
			summary = "행정동별 인구통계 데이터 업데이트",
			description = "행정동 시군구별 총인구수, 인구밀도, 노령화지수, 사업체 수, 총 주택수 정보를 테이블에 업데이트합니다."
	)
	@PostMapping("/admin/population")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ResponseMessage.CustomMessage> updatePopulationInfo(
			@RequestParam(defaultValue = "2022")
			int year
	) {

		houseService.updatePopulation(year);
		return ResponseMessage.responseBasicEntity(
				HouseResoponseCode.POPULATION_UPDATED
		);
	}
}
