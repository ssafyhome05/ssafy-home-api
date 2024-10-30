package com.ssafyhome.model.dao.mapper;

import com.ssafyhome.model.entity.mysql.CategoryEntity;
import com.ssafyhome.model.entity.mysql.SpotEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SpotMapper {

    List<CategoryEntity> getCategories();
    List<SpotEntity> getSpotsByCategory(@Param("dongCode") String dongCode, @Param("category") String category);
    void insertSpots(List<SpotEntity> spots);
}