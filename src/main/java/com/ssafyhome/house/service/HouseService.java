package com.ssafyhome.house.service;

import java.util.List;
import java.util.Map;

import com.ssafyhome.house.dto.HouseGraphDto;
import com.ssafyhome.house.entity.PopulationEntity;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ssafyhome.house.dto.HouseDealDto;
import com.ssafyhome.house.dto.HouseDto;

public interface HouseService {
    List<HouseDto> getHouseInfo(Map<String, Object> params);

    List<HouseDealDto> getHouseDealList(String houseSeq, int page, int limit);

    List<HouseGraphDto> getHouseGraph(String houseSeq, int year);

    String startHouseInfoTask(int dealYmd, int startCd, int endCd);
    String startPopulationTask(String year);

    SseEmitter getSseEmitter(String requestId);
    
	PopulationEntity getPopulation(@Param("dongCode")String dongCode);

    
}
