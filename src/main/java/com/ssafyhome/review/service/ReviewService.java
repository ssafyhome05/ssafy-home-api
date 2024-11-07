package com.ssafyhome.review.service;

import com.ssafyhome.review.dto.ReviewDto;
import com.ssafyhome.review.dto.ReviewSearchDto;

import java.util.List;

public interface ReviewService {

	void addReview(ReviewDto reviewDto);
	List<ReviewDto> getReviewList(ReviewSearchDto reviewSearchDto);
	void updateReview(String aptSeq, ReviewDto reviewDto);
	void deleteReview(String aptSeq);
}
