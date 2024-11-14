package com.ssafyhome.house.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafyhome.common.entity.DongCodeEntity;
import com.ssafyhome.house.dto.HouseDealDto;
import com.ssafyhome.house.dto.HouseDto;
import com.ssafyhome.house.dto.HouseGraphDto;
import com.ssafyhome.house.entity.HouseDealEntity;
import com.ssafyhome.house.entity.HouseInfoEntity;
import com.ssafyhome.house.entity.PopulationEntity;

@Mapper
public interface HouseMapper {
//    List<HouseDto> getHouseInfo(@Param("dongCode") String dongCode);
	List<HouseDto> getHouseInfo(Map<String, Object> params);
    List<HouseDealDto> getHouseDealList(@Param("houseSeq") String houseSeq,
                                      @Param("limit") int limit,
                                      @Param("offset") int offset);
	List<HouseGraphDto> getHouseGraph(@Param("houseSeq") String houseSeq, @Param("year") int year);
	void insertHouseInfo(List<HouseInfoEntity> infoEntityList);
	void insertHouseDeal(List<HouseDealEntity> dealEntityList);
	List<DongCodeEntity> getSidoGugun(String sggCd);
	List<Integer> getLawdCdList(@Param("startCd") String startCd, @Param("endCd") String endCd);
	Set<String> getExistAptSeq(String sggCode);
	
	void insertPopulation(List<PopulationEntity> populationEntityList);
	List<PopulationEntity> getPopulationList();
}
