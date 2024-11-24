package com.ssafyhome.user.response;

import com.ssafyhome.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserResponseCode implements ResponseCode {

	//200 OK
	OK(200020, "사용자 정보 조회 성공", HttpStatus.OK),
	FIND_ID(200021, "아이디 찾기 성공", HttpStatus.OK),
	VERIFICATION_SUCCESS(200023, "이메일 인증 성공", HttpStatus.OK),
	NOT_DUPLICATE(200024, "아이디 사용 가능", HttpStatus.OK),
	PASSWORD_CHANGED(200025, "비밀번호 변경 성공", HttpStatus.OK),
	USER_UPDATED(204026, "회원정보 수정 성공", HttpStatus.OK),
	//201 CREATED
	USER_CREATED(201020, "회원가입 성공", HttpStatus.CREATED),
	MAIL_SEND_SUCCESS(201021, "메일이 성공적으로 전송되었습니다", HttpStatus.CREATED),
	//204 NO_CONTENT
	USER_DELETED(204020, "회원정보 삭제", HttpStatus.OK),

	//400 BAD_REQUEST
	INVALID_PASSWORD(400020, "올바르지 않은 패스워드", HttpStatus.BAD_REQUEST),
	INVALID_VERIFICATION_CODE(400021, "올바르지 않은 인증번호", HttpStatus.BAD_REQUEST),
	//404 NOT_FOUND
	USER_NOT_FOUND(404020, "해당 유저가 존재하지 않습니다", HttpStatus.NOT_FOUND),
	//409 CONFLICT
	ID_DUPLICATED(409020, "이미 존재하는 아이디입니다.", HttpStatus.CONFLICT),

	//500 INTERNAL_SERVER_ERROR
	MAIL_SEND_FAILED(50000, "메일 전송에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
	ENCRYPTION_FAILED(50001,"암호화 실패", HttpStatus.INTERNAL_SERVER_ERROR),
	DECRYPTION_FAILED(50002, "복호화 실패", HttpStatus.INTERNAL_SERVER_ERROR),

	//Enum end
	;

	private final int code;
	private final String message;
	private final HttpStatus httpStatus;
}
