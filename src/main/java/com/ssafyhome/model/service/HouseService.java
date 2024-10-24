package com.ssafyhome.model.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface HouseService {

	String startHouseInfoTask(int dealYmd, int startCd, int endCd);
	SseEmitter getHouseInfoTask(String requestId);
}
