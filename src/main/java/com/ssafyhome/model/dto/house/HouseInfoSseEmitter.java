package com.ssafyhome.model.dto.house;

import lombok.Data;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Data
public class HouseInfoSseEmitter {

	private SseEmitter emitter;
	private int totalRows;
	private String TaskName;

	public HouseInfoSseEmitter(SseEmitter emitter) {

		this.emitter = emitter;
	}
}
