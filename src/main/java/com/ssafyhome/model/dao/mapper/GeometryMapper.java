package com.ssafyhome.model.dao.mapper;

import com.ssafyhome.model.entity.mysql.GeometryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GeometryMapper {

    GeometryEntity selectByDongCode(String dongCode);
    List<String> selectDongCodes();
    void update(GeometryEntity geometryEntity);
}
