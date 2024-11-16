package com.ssafyhome.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCode {

	OK(20000, "OK", HttpStatus.OK),
	FAIL_SEND_MAIL(50000, "메일 전송에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR);

	private final int code;
	private final String massage;
	private final HttpStatus httpStatus;

	ResponseCode(int code, String massage, HttpStatus httpStatus) {

		this.code = code;
		this.massage = massage;
		this.httpStatus = httpStatus;
	}
}
