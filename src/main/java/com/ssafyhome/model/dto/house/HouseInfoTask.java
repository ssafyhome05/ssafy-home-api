package com.ssafyhome.model.dto.house;

import lombok.Data;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Data
public class HouseInfoTask {

	private SseEmitter emitter;
	private int totalRows;
	private String TaskName;

	public HouseInfoTask(SseEmitter emitter) {

		this.emitter = emitter;
	}
}
