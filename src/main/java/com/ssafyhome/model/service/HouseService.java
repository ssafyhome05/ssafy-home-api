package com.ssafyhome.model.service;

import com.ssafyhome.model.dto.house.HouseDealsDto;
import com.ssafyhome.model.dto.house.HouseDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

public interface HouseService {
    List<HouseDto> getHouseInfo(Map<String, Object> params);

    List<HouseDealsDto> getHouseDeals(String houseSeq, int page, int limit);

    String startHouseInfoTask(int dealYmd, int startCd, int endCd);

    SseEmitter getSseEmitter(String requestId);
}
