package com.ssafyhome.ai.dao;

import com.ssafyhome.ai.dto.SpotStatEntity;
import com.ssafyhome.house.entity.HouseDealEntity;
import com.ssafyhome.house.entity.HouseInfoEntity;
import com.ssafyhome.house.entity.PopulationEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AiMapper {

	List<HouseInfoEntity> getHouseInfoEntityList(String UserSeq);
	List<HouseDealEntity> getHouseDealEntityList(List<String> aptList);
	List<PopulationEntity> getPopulationEntityList(List<String> sggCdList);
	List<SpotStatEntity> getSpotStatEntityList(List<String> umdCdList);
}
