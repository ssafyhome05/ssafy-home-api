package com.ssafyhome.house.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ssafyhome.house.dto.HouseDealDto;
import com.ssafyhome.house.dto.HouseDto;

public interface HouseService {
    List<HouseDto> getHouseInfo(Map<String, Object> params);

    List<HouseDealDto> getHouseDealList(String houseSeq, int page, int limit);

    String startHouseInfoTask(int dealYmd, int startCd, int endCd);

    SseEmitter getSseEmitter(String requestId);
}
