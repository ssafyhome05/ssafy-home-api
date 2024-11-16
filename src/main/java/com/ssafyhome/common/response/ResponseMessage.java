package com.ssafyhome.common.response;

import lombok.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseMessage<T> {

	private final HttpHeaders headers;
	private final CustomMessage body;
	private final HttpStatus httpStatus;

	@Data
	@NoArgsConstructor
	public class CustomMessage {

		private int code;
		private String message;

		public CustomMessage(ResponseCode responseCode) {

			this.code = responseCode.getCode();
			this.message = responseCode.getMassage();
		}
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public class CustomMessageWithData extends CustomMessage {

		private T data;

		public CustomMessageWithData(ResponseCode responseCode, T data) {

			super(responseCode);
			this.data = data;
		}
	}

	@Builder
	public ResponseMessage(HttpHeaders headers, ResponseCode responseCode, T data) {

		this.headers = headers;
		if (data == null) {
			this.body = new CustomMessage(responseCode);
		}
		else {
			this.body = new CustomMessageWithData(responseCode, data);
		}
		this.httpStatus = responseCode.getHttpStatus();
	}

	public ResponseEntity<ResponseMessage.CustomMessage> responseEntity() {

		if (headers == null) {
			return ResponseEntity.status(this.httpStatus).body(this.body);
		}
		else {
			return ResponseEntity.status(this.httpStatus).headers(this.headers).body(this.body);
		}
	}
}
