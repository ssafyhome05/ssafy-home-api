package com.ssafyhome.model.dao.mapper;

import com.ssafyhome.model.dto.api.TMapPoint;
import com.ssafyhome.model.entity.mysql.CategoryEntity;
import com.ssafyhome.model.entity.mysql.CustomSpotEntity;
import com.ssafyhome.model.entity.mysql.NearestSpotEntity;
import com.ssafyhome.model.entity.mysql.SpotEntity;
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