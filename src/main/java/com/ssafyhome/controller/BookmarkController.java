package com.ssafyhome.controller;

import com.ssafyhome.model.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(
		name = "Bookmark Controller",
		description = "관심 지역, 관심 매물, 사용자 장소"
)
@RestController
@RequestMapping("/bookmark")
public class BookmarkController {

	@Operation(
			summary = "관심지역 등록",
			description = "사용자가 보고 있는 String 객체의 dongcode 를 받아 북마크등록"
	)
	@PostMapping("/location")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> addLocation(
			@RequestBody
			String dongCode


	) {

		return null;
	}

	@Operation(
			summary = "관심매물 등록",
			description = "String 객체의 houseId 로 북마크등록"
	)
	@PostMapping("/house")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> addHouse(
			@RequestBody
			String houseId
	) {

		return null;
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

		return null;
	}

	@Operation(
			summary = "관심지역 목록 조회",
			description = "북마크한 List<LocationDto> 반환"
	)
	@GetMapping("/location")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<LocationDto>> getLocations() {

		return null;
	}

	@Operation(
			summary = "관심 매물 목록 조회",
			description = "북마크한 List<HouseDto> 반환"
	)
	@GetMapping("/house")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<HouseDto>> getHouses() {

		return null;
	}

	@Operation(
			summary = "사용자 장소 조회",
			description = "북마크한 List<CustomSpotDto> 반환"
	)
	@GetMapping("/custom")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<CustomSpotDto>> getCustomSpots() {

		return null;
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

		return null;
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

		return null;
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

		return null;
	}
}
