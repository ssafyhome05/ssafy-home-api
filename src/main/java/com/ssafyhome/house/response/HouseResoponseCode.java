package com.ssafyhome.house.response;

import com.ssafyhome.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HouseResoponseCode implements ResponseCode {

	//200 OK
	OK(200050, "데이터 조회 성공", HttpStatus.OK),
	//201 CREATED
	TASK_STATUS_CREATED(201050, "데이터 저장 태스크 시작", HttpStatus.CREATED),
	POPULATION_UPDATED(201051, "인구 통계 데이터 업데이트 성공", HttpStatus.CREATED),


	//Enum end
	;

	private final int code;
	private final String message;
	private final HttpStatus httpStatus;
}
