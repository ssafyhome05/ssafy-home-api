package com.ssafyhome.common.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.PrintWriter;

public class ResponseMessage<T> {

	private final HttpHeaders headers;
	private final CustomMessage body;
	private final HttpStatus httpStatus;

	@Data
	@NoArgsConstructor
	public static class CustomMessage {

		private int code;
		private String message;

		public CustomMessage(ResponseCode responseCode) {

			this.code = responseCode.getCode();
			this.message = responseCode.getMessage();
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

	public void setResponse(HttpServletResponse response) {

		if (headers != null) {
			for (String key : this.headers.keySet()) {
				if (this.headers.get(key) != null) {
					for (String value : this.headers.get(key)) {
						response.setHeader(key, value);
					}
				}

			}
		}

		response.setContentType("application/json;charset=utf-8");
		try {
			PrintWriter writer = response.getWriter();
			ObjectMapper mapper = new ObjectMapper();
			writer.write(mapper.writeValueAsString(this.body));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		response.setStatus(this.httpStatus.value());
	}
}
