package com.ssafyhome.model.service.impl;

import com.ssafyhome.model.dto.ReviewDto;
import com.ssafyhome.model.dto.ReviewSearchDto;
import com.ssafyhome.model.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Override
	public void registerReview(ReviewDto reviewDto) {

	}

	@Override
	public List<ReviewDto> getReviews(ReviewSearchDto reviewSearchDto) {
		return List.of();
	}

	@Override
	public void updateReview(long reviewSeq, ReviewDto reviewDto) {

	}

	@Override
	public void deleteReview(long reviewSeq) {

	}
}
