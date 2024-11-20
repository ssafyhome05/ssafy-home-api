package com.ssafyhome.house.service;

import java.util.List;

import com.ssafyhome.house.dto.HouseGraphDto;
import org.apache.ibatis.annotations.Param;

import com.ssafyhome.common.api.news.dto.NewsDto;
import com.ssafyhome.house.dto.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface HouseService {
    List<HouseDto> getHouseInfo(HouseSearchWithTimeDto searchDto);

    List<HouseDealDto> getHouseDealList(String houseSeq, int page, int limit);

    List<HouseGraphDto> getHouseGraph(String houseSeq, int year);

    String startHouseInfoTask(int dealYmd, int startCd, int endCd);
    void updatePopulationTask(int year);

    SseEmitter getSseEmitter(String requestId);
    PopulationDto getPopulation(@Param("dongCode")String dongCode);
    void saveSearchKeyword(String dongCode);

    TopTenDto getTopTen();
    NewsDto getNews();
}
