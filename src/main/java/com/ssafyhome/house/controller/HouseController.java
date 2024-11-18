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
import com.ssafyhome.house.entity.PopulationEntity;
import com.ssafyhome.house.service.HouseService;
import com.ssafyhome.spot.dto.LocationStatusDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
			summary = "특정 연도의 특정 매물의 월별 시세 변동 그래프",
			description = "특정 매물의 지금까지 월 별 거래 데이터 추이를 반환합니다."
	)
	@GetMapping("/status")
	public ResponseEntity<List<HouseGraphDto>> getGraphInfo(
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

		return new ResponseEntity<>(houseService.getHouseGraph(houseSeq, year), HttpStatus.OK);
	}

	@Operation(
			summary = "매물별 모든 거래 내역 반환",
			description = "특정 매물의 거래금액을 반환합니다."
	)
	@GetMapping("/deal")
	public ResponseEntity<List<HouseDealDto>> getHouseDeals(
			@RequestParam("houseSeq")
			String houseSeq,
			@RequestParam("page")
			int page,
			@RequestParam("limit")
			int limit
	) {

		return new ResponseEntity<>(houseService.getHouseDealList(houseSeq, page, limit), HttpStatus.OK);
	}





	@Operation(
			summary = "동코드별 인구통계 반환",
			description = "지역별 총인구, 가구총계, 노령화지수, 평균연령을 반환합니다."
	)
	@GetMapping("/status")
	public ResponseEntity<PopulationEntity> getPopulation(
			@Parameter(
			          name = "dongCode"
			      )
			@RequestParam("dongCode")
			String dongCode
	) {
		return new ResponseEntity<>(houseService.getPopulation(dongCode), HttpStatus.OK);
	}

	@Operation(
			summary = "특정 지역의 매물 존재정보 반환",
			description = "사용자가 입력한 dongcode(필수) 와 세부사항(연도, 매물이름) 을 기준으로 매물 존재내역을 반환합니다."
	)
	@GetMapping("")
	public ResponseEntity<List<HouseDto>> getHouseInfo(
			@Parameter(example = "1111010100")
			@RequestParam("dongCode") 
			String dongCode,
			@RequestParam(value = "startDate", required = false) 
			String startDate,
			@RequestParam(value = "endDate", required = false) 
			String endDate,
			@RequestParam(value = "keyWord", required = false) String keyWord,
			@RequestParam(value = "userSeq", required = false) String userSeq
	) {
		Map<String, Object> params = new HashMap<>();

		params.put("dongCode", dongCode);

		if (startDate != null && !startDate.isEmpty()) {
			String[] start = startDate.split("-");
			params.put("startYear", start[0]);
			params.put("startMonth", start[1]);
		}else{
			params.put("startYear", "");
			params.put("startMonth" , "");
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

		if(userSeq != null && !userSeq.isEmpty()){
			params.put("userSeq", userSeq);
		}else{
			params.put("userSeq", "");
		}

		return new ResponseEntity<>(houseService.getHouseInfo(params), HttpStatus.OK);
	}

	@Operation(
			summary = "지도 상 법정동 경계선 반환",
			description = "검색한 법정동의 경계면 좌표 리스트를 반환합니다."
	)
	@GetMapping("/polygon")
	public List<Point> getDongPolygon(
			@RequestParam("dongCode")
			@Parameter(
			          name = "dongCode",
			          example = "1111010100"
			      )
			String dongCode
	){
		List<Point> dongPolygonList = geometryUtil.getPoints(geometryMapper.selectByDongCode(dongCode));
		return dongPolygonList;
	}

	@Operation(
			summary = "새로운 매물 데이터 업데이트 (관리자) ",
			description = "신규 매물 거래 내역을 업데이트 합니다. (관리자) "
	)
	@PostMapping("/admin/register")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updateHouseInfo(
			@RequestParam(name="startCd", required = false, defaultValue = "11110")
			int startCd,

			@RequestParam(name="endCd", required = false, defaultValue = "60000")
			int endCd,

			@RequestParam(name="dealYmd")
			int dealYmd
	) {

		String requestId = houseService.startHouseInfoTask(dealYmd, startCd, endCd);
		return new ResponseEntity<>(requestId, HttpStatus.CREATED);
	}

	@Operation(
			summary = "새로운 매물 데이터 업데이트 진척도 확인 (관리자)",
			description = "신규 매물 거래 내역의 업데이트 진척도를 반환합니다. (관리자전용)"
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
			description = "행정동 시군구별 총인구수, 인구밀도, 노령화지수, 사업체 수, 총 주택수 정보를 테이블에 업데이트합니다."
	)
	@GetMapping("/admin/register")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updatePopulationInfo() {

		String result = houseService.startPopulationTask("2022");
		if (!result.equals("success")) {
			//excpeion 구현 필요
		}
		return new ResponseEntity<>("success", HttpStatus.CREATED);
	}
}
