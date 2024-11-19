package com.ssafyhome.navigate.response;

import com.ssafyhome.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NavigateResponseCode implements ResponseCode {

	//200 OK
	OK(200040, "경로 및 소요시간 응답 성공", HttpStatus.OK),

	//Enum end
	;

	private final int code;
	private final String message;
	private final HttpStatus httpStatus;
}
