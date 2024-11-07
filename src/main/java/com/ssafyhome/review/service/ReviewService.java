package com.ssafyhome.review.service;

import com.ssafyhome.review.dto.ReviewDto;
import com.ssafyhome.review.dto.ReviewSearchDto;

import java.util.List;

public interface ReviewService {

	void registerReview(ReviewDto reviewDto);
	List<ReviewDto> getReviews(ReviewSearchDto reviewSearchDto);
	void updateReview(String aptSeq, ReviewDto reviewDto);
	void deleteReview(String aptSeq);
}
