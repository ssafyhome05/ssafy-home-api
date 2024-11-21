package com.ssafyhome.house.code;

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
	KEYWORD_SUCCESS_SAVED(201052, "검색어가 성공적으로 저장되었습니다.", HttpStatus.CREATED),
	//404 NOT_FOUND
	REQUEST_ID_NOT_FOUND(404050, "해당하는 진행 작업을 찾을 수 없습니다", HttpStatus.NOT_FOUND),


	//Enum end
	;

	private final int code;
	private final String message;
	private final HttpStatus httpStatus;
}
