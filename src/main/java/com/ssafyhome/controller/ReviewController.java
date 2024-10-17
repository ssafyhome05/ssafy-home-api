package com.ssafyhome.controller;

import com.ssafyhome.model.dto.ReviewSearchDto;
import com.ssafyhome.model.entity.mysql.ReviewEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

	@Operation(
			summary = "평가 등록",
			description = "ReviewEntity 등록"
	)
	@PostMapping("/")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> registerReview(
			@RequestBody
			ReviewEntity reviewEntity
	) {

		return null;
	}

	@Operation(
			summary = "리뷰 전체목록 조회",
			description = "모든 ReviewEntity 를 List<ReviewEntity> 로 반환"
	)
	@GetMapping("/list")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<ReviewEntity>> getReview(
			@RequestBody
			ReviewSearchDto reviewSearchDto
	) {

		return null;
	}

	@Operation(
			summary = "리뷰 수정",
			description = "reviewSeq 와 일치하는 ReviewEntity 객체 수정"
	)
	@PutMapping("/{reviewSeq}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> updateReview(
			@PathVariable
			String reviewSeq,

			@RequestBody
			ReviewEntity reviewEntity
	) {

		return null;
	}

	@Operation(
			summary = "리뷰 삭제",
			description = "reviewSeq 와 일치하는 ReviewEntity 객체 삭제"
	)
	@DeleteMapping("/{reviewSeq}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> deleteReview(
			@PathVariable
			String reviewSeq
	) {

		return null;
	}
}
