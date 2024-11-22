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
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class CustomMessage {

		private int code;
		private String message;

		private CustomMessage(ResponseCode responseCode) {

			this.code = responseCode.getCode();
			this.message = responseCode.getMessage();
		}
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public class CustomMessageWithData extends CustomMessage {

		private T data;

		private CustomMessageWithData(ResponseCode responseCode, T data) {

			super(responseCode);
			this.data = data;
		}
	}

	private ResponseMessage(ResponseCode responseCode, HttpHeaders headers, T data) {

		this.headers = headers;
		if (data == null) {
			this.body = new CustomMessage(responseCode);
		}
		else {
			this.body = new CustomMessageWithData(responseCode, data);
		}
		this.httpStatus = responseCode.getHttpStatus();
	}

	private ResponseEntity<ResponseMessage.CustomMessage> makeResponseEntity() {

		if (headers == null) {
			return ResponseEntity.status(this.httpStatus).body(this.body);
		}
		else {
			return ResponseEntity.status(this.httpStatus).headers(this.headers).body(this.body);
		}
	}

	private void setResponse(HttpServletResponse response) {

		if (headers != null) {
			for (String key : this.headers.keySet()) {
				if (this.headers.get(key) != null) {
					for (String value : this.headers.get(key)) {
						response.setHeader(key, value);
					}
				}
			}
		}

		response.setStatus(this.httpStatus.value());

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
	}

	public static <E> ResponseEntity<CustomMessage> responseFullEntity(ResponseCode responseCode, HttpHeaders headers, E data) {

		return new ResponseMessage<>(responseCode, headers, data).makeResponseEntity();
	}

	public static ResponseEntity<CustomMessage> responseBasicEntity(ResponseCode responseCode) {

		return responseFullEntity(responseCode, null, null);
	}

	public static ResponseEntity<CustomMessage> responseHeadersEntity(ResponseCode responseCode, HttpHeaders headers) {

		return responseFullEntity(responseCode, headers, null);
	}

	public static <E> ResponseEntity<CustomMessage> responseDataEntity(ResponseCode responseCode, E data) {

		return responseFullEntity(responseCode, null, data);
	}

	public static <E> void setFullResponse(HttpServletResponse response, ResponseCode responseCode, HttpHeaders headers, E data) {

		new ResponseMessage<>(responseCode, headers, data).setResponse(response);
	}

	public static void setBasicResponse(HttpServletResponse response, ResponseCode responseCode) {

		setFullResponse(response, responseCode, null, null);
	}

	public static void setHeadersResponse(HttpServletResponse response, ResponseCode responseCode, HttpHeaders headers) {

		setFullResponse(response, responseCode, headers, null);
	}

	public static <E> void setDataResponse(HttpServletResponse response, ResponseCode responseCode, E data) {

		setFullResponse(response, responseCode, null, data);
	}
}
