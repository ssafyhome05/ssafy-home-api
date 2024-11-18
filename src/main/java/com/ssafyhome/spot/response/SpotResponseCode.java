package com.ssafyhome.spot.response;

import com.ssafyhome.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SpotResponseCode implements ResponseCode {

	//200 OK
	OK(200030, "현재 지역의 편의시설 호출 성공", HttpStatus.OK),

	//Enum end
	;

	private final int code;
	private final String message;
	private final HttpStatus httpStatus;
}
