package com.ssafyhome.model.service;

import com.ssafyhome.model.dto.review.ReviewDto;
import com.ssafyhome.model.dto.review.ReviewSearchDto;

import java.util.List;

public interface ReviewService {

	void registerReview(ReviewDto reviewDto);
	List<ReviewDto> getReviews(ReviewSearchDto reviewSearchDto);
	void updateReview(String aptSeq, ReviewDto reviewDto);
	void deleteReview(String aptSeq);
}
