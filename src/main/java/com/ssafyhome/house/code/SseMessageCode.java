package com.ssafyhome.house.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SseMessageCode {

	START_TASK(20100, "태스크 시작"),
	TASK_COMPLETED(20000, "태스크 단위 완료"),
	NOT_FOUND_JIBUN(40400, "지번을 찾을 수 없음"),
	API_ERROR(40000, "외부 API에서 오류가 발생해 데이터를 가져오지 못함"),
	TASK_SPEND_TIME(20001, "전체 태스크 소요 시간"),
	TASK_FINISHED(20400, "태스크 종료"),
	;

	private final int code;
	private final String message;


}
