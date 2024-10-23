package com.ssafyhome.model.service.impl;

import com.ssafyhome.model.dao.mapper.ReviewMapper;
import com.ssafyhome.model.dto.review.ReviewDto;
import com.ssafyhome.model.dto.review.ReviewSearchDto;
import com.ssafyhome.model.entity.mysql.ReviewEntity;
import com.ssafyhome.model.service.ReviewService;
import com.ssafyhome.util.ConvertUtil;
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
