package com.ssafyhome.controller;

import com.ssafyhome.model.dto.ReviewDto;
import com.ssafyhome.model.dto.ReviewSearchDto;
import com.ssafyhome.model.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
		name = "Review Controller",
		description = "부동산 매물에 대한 평점 및 평가 부여"
)
@RestController
@RequestMapping("/review")
public class ReviewController {

	private final ReviewService reviewService;

	public ReviewController(ReviewService reviewService) {

		this.reviewService = reviewService;
	}

	@Operation(
			summary = "평가 등록",
			description = "ReviewEntity 등록"
	)
	@PostMapping("/")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> registerReview(
			@RequestBody
			ReviewDto reviewDto
	) {

		reviewService.registerReview(reviewDto);
		return new ResponseEntity<>("review register success", HttpStatus.CREATED);
	}

	@Operation(
			summary = "리뷰 전체목록 조회",
			description = "모든 ReviewEntity 를 List<ReviewEntity> 로 반환"
	)
	@GetMapping("/list")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<ReviewDto>> getReview(
			@RequestBody
			ReviewSearchDto reviewSearchDto
	) {

		List<ReviewDto> reviewDtoList = reviewService.getReviews(reviewSearchDto);
		return new ResponseEntity<>(reviewDtoList, HttpStatus.OK);
	}

	@Operation(
			summary = "리뷰 수정",
			description = "reviewSeq 와 일치하는 ReviewEntity 객체 수정"
	)
	@PutMapping("/{reviewSeq}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> updateReview(
			@PathVariable
			long reviewSeq,

			@RequestBody
			ReviewDto reviewDto
	) {

		reviewService.updateReview(reviewSeq, reviewDto);
		return new ResponseEntity<>("review update success", HttpStatus.OK);
	}

	@Operation(
			summary = "리뷰 삭제",
			description = "reviewSeq 와 일치하는 ReviewEntity 객체 삭제"
	)
	@DeleteMapping("/{reviewSeq}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> deleteReview(
			@PathVariable
			long reviewSeq
	) {

		reviewService.deleteReview(reviewSeq);
		return new ResponseEntity<>("review delete success", HttpStatus.OK);
	}
}
