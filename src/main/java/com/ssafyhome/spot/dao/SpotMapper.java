package com.ssafyhome.spot.dao;

import com.ssafyhome.common.api.tmap.dto.TMapPoint;
import com.ssafyhome.spot.entity.CategoryEntity;
import com.ssafyhome.bookmark.entity.CustomSpotEntity;
import com.ssafyhome.spot.entity.NearestSpotEntity;
import com.ssafyhome.spot.entity.SpotEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SpotMapper {

    TMapPoint getTMapPointByAptSeq(String aptSeq);
    List<CategoryEntity> getCategories();
    List<SpotEntity> getSpotsByCategory(@Param("dongCode") String dongCode, @Param("category") String category);
    void insertSpots(List<SpotEntity> spots);
    void insertNearestSpots(List<NearestSpotEntity> spots);
    List<CustomSpotEntity> getCustomSpotByUser(String userSeq);
    List<NearestSpotEntity> getNearestSpotByStartPoint(String aptSeq);
}