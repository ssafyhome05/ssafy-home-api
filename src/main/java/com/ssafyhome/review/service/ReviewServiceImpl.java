package com.ssafyhome.review.service;

import com.ssafyhome.review.dao.ReviewMapper;
import com.ssafyhome.review.dto.ReviewDto;
import com.ssafyhome.review.dto.ReviewSearchDto;
import com.ssafyhome.review.entity.ReviewEntity;
import com.ssafyhome.common.util.ConvertUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

	private final ReviewMapper reviewMapper;
	private final ConvertUtil convertUtil;

	public ReviewServiceImpl(
			ReviewMapper reviewMapper,
			ConvertUtil convertUtil
	) {

		this.reviewMapper = reviewMapper;
		this.convertUtil = convertUtil;
	}

	@Override
	public void registerReview(ReviewDto reviewDto) {

		ReviewEntity reviewEntity = convertUtil.convert(reviewDto, ReviewEntity.class);
		reviewEntity.setUserSeq(getUserSeq());
		reviewMapper.insertReview(reviewEntity);
	}

	@Override
	public List<ReviewDto> getReviews(ReviewSearchDto reviewSearchDto) {

		List<ReviewEntity> reviewEntityList = reviewMapper.getReviewBySearchDto(reviewSearchDto);
		return convertUtil.convert(reviewEntityList, ReviewDto.class);
	}

	@Override
	public void updateReview(String aptSeq, ReviewDto reviewDto) {

		ReviewEntity reviewEntity = convertUtil.convert(reviewDto, ReviewEntity.class);
		reviewEntity.setAptSeq(aptSeq);
		reviewEntity.setUserSeq(getUserSeq());
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
