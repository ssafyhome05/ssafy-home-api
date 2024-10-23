package com.ssafyhome.model.dao.mapper;

import com.ssafyhome.model.entity.mysql.DongCodeEntity;
import com.ssafyhome.model.entity.mysql.HouseDealEntity;
import com.ssafyhome.model.entity.mysql.HouseInfoEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HouseMapper {

	void insertHouseInfo(HouseInfoEntity houseInfoEntity);
	void insertHouseDeal(HouseDealEntity houseDealEntity);
	DongCodeEntity getSidoGugun(String dongCode);
	List<Integer> getLawdCdList();
	boolean isExistHouseInfo(String houseSeq);
}
