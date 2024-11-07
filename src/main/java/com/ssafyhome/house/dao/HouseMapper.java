package com.ssafyhome.house.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafyhome.common.entity.DongCodeEntity;
import com.ssafyhome.house.dto.HouseDealDto;
import com.ssafyhome.house.dto.HouseDto;
import com.ssafyhome.house.entity.HouseDealEntity;
import com.ssafyhome.house.entity.HouseInfoEntity;

@Mapper
public interface HouseMapper {
//    List<HouseDto> getHouseInfo(@Param("dongCode") String dongCode);
	List<HouseDto> getHouseInfo(Map<String, Object> params);
    List<HouseDealDto> getHouseDealList(@Param("houseSeq") String houseSeq,
                                      @Param("limit") int limit,
                                      @Param("offset") int offset);
	void insertHouseInfo(List<HouseInfoEntity> infoEntityList);
	void insertHouseDeal(List<HouseDealEntity> dealEntityList);
	DongCodeEntity getSidoGugun(String dongCode);
	List<Integer> getLawdCdList(@Param("startCd") String startCd, @Param("endCd") String endCd);
	boolean isExistHouseInfo(String houseSeq);
	Set<String> getExistAptSeq(String sggCode);
}
