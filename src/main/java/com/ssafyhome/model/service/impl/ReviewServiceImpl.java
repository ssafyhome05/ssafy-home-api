package com.ssafyhome.model.service.impl;

import com.ssafyhome.model.dao.mapper.ReviewMapper;
import com.ssafyhome.model.dto.ReviewDto;
import com.ssafyhome.model.dto.ReviewSearchDto;
import com.ssafyhome.model.entity.mysql.ReviewEntity;
import com.ssafyhome.model.service.ReviewService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

	private final ReviewMapper reviewMapper;

	public ReviewServiceImpl(ReviewMapper reviewMapper) {

		this.reviewMapper = reviewMapper;
	}

	@Override
	public void registerReview(ReviewDto reviewDto) {

		ReviewEntity reviewEntity = ReviewEntity.builder()
				.aptSeq(reviewDto.getAptSeq())
				.userSeq(getUserSeq())
				.reviewTitle(reviewDto.getReviewTitle())
				.reviewContent(reviewDto.getReviewContent())
				.reviewRate(reviewDto.getReviewRate())
				.build();
		reviewMapper.insertReview(reviewEntity);
	}

	@Override
	public List<ReviewDto> getReviews(ReviewSearchDto reviewSearchDto) {

		List<ReviewEntity> reviewEntityList = reviewMapper.getReviewBySearchDto(reviewSearchDto);
		return reviewEntityList.stream()
				.map(reviewEntity -> ReviewDto.builder()
						.aptSeq(reviewEntity.getAptSeq())
						.userSeq(getUserSeq())
						.reviewTitle(reviewEntity.getReviewTitle())
						.reviewContent(reviewEntity.getReviewContent())
						.reviewRate(reviewEntity.getReviewRate())
						.createdAt(reviewEntity.getCreatedAt())
						.modifiedAt(reviewEntity.getModifiedAt())
						.build()
				)
				.toList();
	}

	@Override
	public void updateReview(String aptSeq, ReviewDto reviewDto) {

		ReviewEntity reviewEntity = ReviewEntity.builder()
				.aptSeq(aptSeq)
				.userSeq(getUserSeq())
				.reviewTitle(reviewDto.getReviewTitle())
				.reviewContent(reviewDto.getReviewContent())
				.reviewRate(reviewDto.getReviewRate())
				.build();
		reviewMapper.updateReview(reviewEntity);
	}

	@Override
	public void deleteReview(String aptSeq) {

		reviewMapper.deleteReview(aptSeq, getUserSeq());
	}

	private long getUserSeq() {

		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		String userSeq = authentication.getName();
		return Long.parseLong(userSeq);
	}
}
