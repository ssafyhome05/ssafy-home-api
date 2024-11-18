package com.ssafyhome.auth.response;

import com.ssafyhome.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthResponseCode implements ResponseCode {

	//200 - 201 - 204 OK, CREATED, NO_CONTENT
	LOGIN_SUCCESS(200010, "로그인 성공", HttpStatus.OK),
	LOGOUT_SUCCESS(200011, "로그아웃 성공", HttpStatus.OK),
	TOKEN_REISSUED(201011, "jwt token 2종 재발급 성공", HttpStatus.CREATED),

	//404 NOT_FOUND
	REFRESH_TOKEN_NOT_FOUND(404010, "refresh token 없음", HttpStatus.NOT_FOUND),


	//401 - 403 UNAUTHORIZED, FORBIDDEN
	METHOD_UNAUTHORIZED(401010,"해당 메서드에 접근하기 위해 인증 필요", HttpStatus.UNAUTHORIZED),
	INVALID_JWT_TOKEN(401011,"올바르지 않은 형식의 jwt", HttpStatus.UNAUTHORIZED),
	ACCESS_TOKEN_EXPIRED(401012, "access token 기간 만료", HttpStatus.UNAUTHORIZED),
	REFRESH_TOKEN_EXPIRED(401013,"refresh token 기간 만료", HttpStatus.UNAUTHORIZED),
	FAIL_TO_LOGIN(401013,"로그인 실패", HttpStatus.UNAUTHORIZED),
	METHOD_FORBIDDEN(403010,"해당 메서드에 접근하기 위해 권한 필요", HttpStatus.FORBIDDEN),


	//500 INTERNAL_SERVER_ERROR
	ENCRYPTION_FAILED(500010,"암호화 실패", HttpStatus.INTERNAL_SERVER_ERROR),
	DECRYPTION_FAILED(500011, "복호화 실패", HttpStatus.INTERNAL_SERVER_ERROR),

		//Enum end
	;

	private final int code;
	private final String message;
	private final HttpStatus httpStatus;
}
