package com.ssafyhome.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SgisAccessToken {

	private String id;
	private Result result;
	@JsonProperty("errMsg")
	private String errMsg;
	@JsonProperty("errCd")
	private String errCd;
	@JsonProperty("trId")
	private String trId;

	@Data
	public static class Result {

		@JsonProperty("accessToken")
		private String accessToken;
		@JsonProperty("accessTimeout")
		private String accessTimeout;
	}
}
