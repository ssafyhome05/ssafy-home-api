package com.ssafyhome.common.api.sgis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SgisGeoCode {

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

		private int totalcount;
		private List<ResultData> resultdata;
		private int matching;
		private int pagenum;
		private int returncount;

		@Data
		public static class ResultData {

			private String riNm;
			private String roadNmSubNo;
			private String sggCd;
			private String admCd;
			private String roadNmMainNo;
			private String legCd;
			private String roadNm;
			private String bdMatches;
			private String bdSubNm;
			private String addrType;
			private String sidoNm;
			private String sggNm;
			private String sidoCd;
			private String roadCd;
			private String bdMainNm;
			private String admNm;
			private String jibunMainNo;
			private String originXy;
			private String jibunSubNo;
			private String riCd;
			private String y;
			private String x;
			private String legNm;
		}
	}
}
