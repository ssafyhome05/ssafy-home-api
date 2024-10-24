package com.ssafyhome.model.service;

<<<<<<< HEAD
import com.ssafyhome.model.dto.house.HouseDealsDto;
import com.ssafyhome.model.dto.house.HouseDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HouseService {
    List<HouseDto> getHouseInfo(String dongCode);
    List<HouseDealsDto> getHouseDeals(String houseSeq, int page, int limit);
=======
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface HouseService {

	String startHouseInfoTask(int dealYmd, int startCd, int endCd);
	SseEmitter getHouseInfoTask(String requestId);
>>>>>>> main
}
