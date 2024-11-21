package com.ssafyhome.house.dto;

import com.ssafyhome.house.code.SseMessageCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Data
public class SseMessageDto<T> {

	private int code;
	private String message;
	private T data;

	public SseMessageDto(SseMessageCode messageCode, T data) {

		this.code = messageCode.getCode();
		this.message = messageCode.getMessage();
		this.data = data;
	}

	public void sendEvent(SseEmitter emitter) throws IOException {

		emitter.send(this, MediaType.APPLICATION_JSON);
	}

	@Data
	@AllArgsConstructor
	public static class Stats {

		private String code;
		private int seq;
		private int size;
	}
}


