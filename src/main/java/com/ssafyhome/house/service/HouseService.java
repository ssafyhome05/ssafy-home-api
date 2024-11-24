package com.ssafyhome.house.service;

import java.util.List;

import com.ssafyhome.common.util.object.Point;
import com.ssafyhome.house.dto.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface HouseService {


    String startHouseInfoTask(int dealYmd, int startCd, int endCd, String clientIpv4, String clientIpv6);
    SseEmitter getSseEmitter(String requestId);

    AdminUpdatedInfoDto updatePopulation(int year, String clientIpv4, String clientIpv6);
    AdminUpdatedInfoDto getRecentLoginInfo(String taskName);
    PopulationDto getPopulation(String dongCode);

    void saveSearchKeyword(String dongCode, String clientIp);
    TopTenDto getTopTen();

    List<HouseDto> getHouseInfo(HouseSearchWithTimeDto searchDto);
    List<HouseDealDto> getHouseDealList(String houseSeq, int page, int limit);
    List<HouseGraphDto> getHouseGraph(String houseSeq, int year);
    List<NewsDto> getNewsList();
    List<Point> getPoints(String dongCode);
}
