package com.ssafyhome.controller;

import com.ssafyhome.model.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<?> addLocation(
			@RequestBody
			String dongCode
	) {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@PostMapping("/house")
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
	public ResponseEntity<List<LocationDto>> getLocations() {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/house")
	public ResponseEntity<List<HouseDto>> getHouses() {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/custom")
	public ResponseEntity<List<CustomSpotDto>> getCustomSpots() {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@GetMapping("/status")
	public ResponseEntity<List<BookmarkStatusDto>> getStatus() {

		return null;
	}

	@Operation(
			summary = "",
			description = ""
	)
	@DeleteMapping("/location/{dongCode}")
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
	public ResponseEntity<?> deleteCustomSpot(
			@PathVariable
			String customSeq
	) {

		return null;
	}
}
