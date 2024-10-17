package com.ssafyhome.model.service;

import com.ssafyhome.model.dto.ReviewDto;
import com.ssafyhome.model.dto.ReviewSearchDto;

import java.util.List;

public interface ReviewService {

	void registerReview(ReviewDto reviewDto);
	List<ReviewDto> getReviews(ReviewSearchDto reviewSearchDto);
	void updateReview(long reviewSeq, ReviewDto reviewDto);
	void deleteReview(long reviewSeq);
}
