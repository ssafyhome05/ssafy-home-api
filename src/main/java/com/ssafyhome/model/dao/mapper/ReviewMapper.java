package com.ssafyhome.model.dao.mapper;

import com.ssafyhome.model.dto.ReviewSearchDto;
import com.ssafyhome.model.entity.mysql.ReviewEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {

	void insertReview(ReviewEntity reviewEntity);
	void updateReview(ReviewEntity reviewEntity);
	void deleteReview(long reviewSeq);
	List<ReviewEntity> getReviewBySearchDto(ReviewSearchDto reviewSearchDto);

}
