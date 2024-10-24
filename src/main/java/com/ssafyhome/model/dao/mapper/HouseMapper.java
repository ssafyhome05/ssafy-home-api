package com.ssafyhome.model.dao.mapper;

import com.ssafyhome.model.dto.house.HouseDealsDto;
import com.ssafyhome.model.dto.house.HouseDto;
import com.ssafyhome.model.entity.mysql.DongCodeEntity;
import com.ssafyhome.model.entity.mysql.HouseDealEntity;
import com.ssafyhome.model.entity.mysql.HouseInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseMapper {
    List<HouseDto> getHouseInfo(@Param("dongCode") String dongCode);
    List<HouseDealsDto> getHouseDeals(@Param("houseSeq") String houseSeq,
                                      @Param("limit") int limit,
                                      @Param("offset") int offset);
	void insertHouseInfo(HouseInfoEntity houseInfoEntity);
	void insertHouseDeal(HouseDealEntity houseDealEntity);
	DongCodeEntity getSidoGugun(String dongCode);
	List<Integer> getLawdCdList(@Param("startCd") String startCd, @Param("endCd") String endCd);
	boolean isExistHouseInfo(String houseSeq);
}
