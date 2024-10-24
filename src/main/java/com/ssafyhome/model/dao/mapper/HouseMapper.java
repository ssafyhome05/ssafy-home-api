package com.ssafyhome.model.dao.mapper;

import com.ssafyhome.model.dto.house.HouseDealsDto;
import com.ssafyhome.model.dto.house.HouseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseMapper {
    List<HouseDto> getHouseInfo(@Param("dongCode") String dongCode);
    List<HouseDealsDto> getHouseDeals(@Param("houseSeq") String houseSeq,
                                      @Param("limit") int limit,
                                      @Param("offset") int offset);
}
