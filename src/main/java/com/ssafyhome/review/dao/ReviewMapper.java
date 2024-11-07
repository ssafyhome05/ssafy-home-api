package com.ssafyhome.review.dao;

import com.ssafyhome.review.dto.ReviewSearchDto;
import com.ssafyhome.review.entity.ReviewEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {

	void insertReview(ReviewEntity reviewEntity);
	void updateReview(ReviewEntity reviewEntity);
	void deleteReview(@Param("aptSeq") String aptSeq, @Param("userSeq") long userSeq);
	List<ReviewEntity> getReviewBySearchDto(ReviewSearchDto reviewSearchDto);

}
