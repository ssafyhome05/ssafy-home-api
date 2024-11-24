package com.ssafyhome.bookmark.controller;

import com.ssafyhome.bookmark.dto.BookmarkStatusDto;
import com.ssafyhome.house.dto.HouseDto;
import com.ssafyhome.spot.dto.CustomSpotDto;
import com.ssafyhome.spot.dto.LocationDto;
import com.ssafyhome.bookmark.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Tag(
		name = "Bookmark Controller",
		description = "관심 지역(location), 관심 매물(house), 사용자 장소(custom spot)"
)
@RestController
@RequestMapping("/bookmark")
public class BookmarkController {

	private static BookmarkService bookmarkService;

	public BookmarkController(BookmarkService bookmarkService) {
		this.bookmarkService = bookmarkService;
	}

	@Operation(
			summary = "관심지역 등록",
			description = "사용자가 보고 있는 String 객체의 dongcode 를 받아 북마크등록"
	)
	@PostMapping("/location")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> addLocation(
			@RequestParam("dongCode")
			String dongCode
	) {

		Map<String, Object> params = new HashMap<>();
		params.put("dongCode", dongCode);
		params.put("userSeq", SecurityContextHolder.getContext().getAuthentication().getName());
		System.out.println(params);
		bookmarkService.addLocationBookmark(params);

		return new ResponseEntity<>("add location bookmark success", HttpStatus.OK);
	}

	@Operation(
			summary = "관심매물 등록",
			description = "String 객체의 houseId 로 북마크등록"
	)
	@PostMapping("/house")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> addHouse(
			@RequestParam(value = "houseId")
			String houseId
	) {

		Map<String, Object> params = new HashMap<>();
		params.put("userSeq", SecurityContextHolder.getContext().getAuthentication().getName());
		params.put("aptSeq", houseId);

		bookmarkService.addHouseBookmark(params);

		return new ResponseEntity<>("add bookmark success", HttpStatus.OK);
	}

	@Operation(
			summary = "사용자 장소 등록",
			description = "CustomSpotDto 객체를 받아 북마크등록"
	)
	@PostMapping("/custom")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> addCustomSpot(
			@RequestBody
			CustomSpotDto customSpotDto
	) {
		Map<String, Object> params = new HashMap<>();
		params.put("userSeq", SecurityContextHolder.getContext().getAuthentication().getName());
		params.put("customSpot", customSpotDto);

		bookmarkService.addCustomSpotBookmark(params);

		return new ResponseEntity<>("add bookmark success", HttpStatus.OK);
	}

	@Operation(
			summary = "관심지역 목록 조회",
			description = "북마크한 List<LocationDto> 반환"
	)
	@GetMapping("/location")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<LocationDto>> getLocationList() {
		Map<String, Object> params = new HashMap<>();
		params.put("userSeq", SecurityContextHolder.getContext().getAuthentication().getName());

		List<LocationDto> locationList = bookmarkService.getLocationList(params);
		
		return new ResponseEntity<>(locationList, HttpStatus.OK);
		
	}

	@Operation(
			summary = "관심 매물 목록 조회",
			description = "북마크한 List<HouseDto> 반환"
	)
	@GetMapping("/house")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<HouseDto>> getHouseList() {
		Map<String, Object> params = new HashMap<>();
		
		params.put("userSeq", SecurityContextHolder.getContext().getAuthentication().getName());

		List<HouseDto> houseList = bookmarkService.getHouseList(params);
		
		return new ResponseEntity<>(houseList, HttpStatus.OK);
	}

	@Operation(
			summary = "사용자 장소 조회",
			description = "북마크한 List<CustomSpotDto> 반환"
	)
	@GetMapping("/custom")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<CustomSpotDto>> getCustomSpotList() {
		Map<String, Object> params = new HashMap<>();
		params.put("userSeq", SecurityContextHolder.getContext().getAuthentication().getName());
		List<CustomSpotDto> customSpotList = bookmarkService.getCustomSpotList(params);
		return new ResponseEntity<>(customSpotList, HttpStatus.OK);
	}

	@Operation(
			summary = "모든 북마크 요약 조회",
			description = "북마크한 매물, 지역, 사용자지역의 세부목록  조회"
	)
	@GetMapping("/status")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<BookmarkStatusDto>> getStatus() {

		return null;
	}

	@Operation(
			summary = "관심지역 삭제",
			description = "String 객체의 dongcode 를 받아 사용자북마크에서 삭제"
	)
	@DeleteMapping("/location/{dongCode}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> deleteLocation(
			@PathVariable
			String dongCode
	) {
		Map<String, Object> params = new HashMap<>();
		params.put("dongCode", dongCode);
		params.put("userSeq", SecurityContextHolder.getContext().getAuthentication().getName());

		bookmarkService.deleteLocationBookmark(params);

		return new ResponseEntity<>("delete location bookmark success", HttpStatus.OK);
	}

	@Operation(
			summary = "관심매물 삭제",
			description = "String 객체의 houseId 를 받아 사용자북마크에서 삭제"
	)
	@DeleteMapping("/house/{houseId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> deleteHouse(
			@PathVariable
			String houseId
	) {

		Map<String, Object> params = new HashMap<>();
		params.put("userSeq", SecurityContextHolder.getContext().getAuthentication().getName());
		params.put("aptSeq", houseId);

		bookmarkService.deleteHouseBookmark(params);

		return new ResponseEntity<>("delete bookmark success", HttpStatus.OK);
	}

	@Operation(
			summary = "사용자장소 삭제",
			description = "String 객체의 customSeq 를 받아 사용자북마크에서 삭제"
	)
	@DeleteMapping("/custom/{customSeq}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> deleteCustomSpot(
			@PathVariable
			String customSeq
	) {

		Map<String, Object> params = new HashMap<>();
		params.put("userSeq", SecurityContextHolder.getContext().getAuthentication().getName());
		params.put("customSeq", customSeq);

		bookmarkService.deleteCustomSpotBookmark(params);

		return new ResponseEntity<>("delete CustomSpot bookmark success", HttpStatus.OK);

	}
}
