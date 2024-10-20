package com.ssafyhome.model.dao.mapper;

import com.ssafyhome.model.dto.ReviewSearchDto;
import com.ssafyhome.model.entity.mysql.ReviewEntity;
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
