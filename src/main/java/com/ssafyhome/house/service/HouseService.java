package com.ssafyhome.house.service;

import java.util.List;

import com.ssafyhome.common.util.object.Point;
import com.ssafyhome.house.dto.HouseGraphDto;

import com.ssafyhome.common.api.news.dto.NewsDto;
import com.ssafyhome.house.dto.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface HouseService {


    String startHouseInfoTask(int dealYmd, int startCd, int endCd);
    SseEmitter getSseEmitter(String requestId);

    void updatePopulation(int year);
    PopulationDto getPopulation(String dongCode);

    void saveSearchKeyword(String dongCode);
    TopTenDto getTopTen();

    List<HouseDto> getHouseInfo(HouseSearchWithTimeDto searchDto);
    List<HouseDealDto> getHouseDealList(String houseSeq, int page, int limit);
    List<HouseGraphDto> getHouseGraph(String houseSeq, int year);
    NewsDto getNews();
    List<Point> getPoints(String dongCode);
}
