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
			description = "사용자가 보고 있는 지역을 관심지역으로 등록"
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
			description = "String 객체의 houseId 로 관심매물 등록"
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
			summary = "",
			description = ""
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
			summary = "",
			description = ""
	)
	@GetMapping("/location")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<LocationDto>> getLocations() {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/house")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<HouseDto>> getHouses() {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/custom")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<CustomSpotDto>> getCustomSpots() {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/status")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<BookmarkStatusDto>> getStatus() {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
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
			summary = "",
			description = ""
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
			summary = "",
			description = ""
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
