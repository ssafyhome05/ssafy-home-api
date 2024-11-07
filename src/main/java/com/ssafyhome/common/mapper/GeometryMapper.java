package com.ssafyhome.common.mapper;

import com.ssafyhome.common.entity.GeometryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GeometryMapper {

    GeometryEntity selectByDongCode(String dongCode);
    List<String> selectDongCodes();
    void update(GeometryEntity geometryEntity);
}
