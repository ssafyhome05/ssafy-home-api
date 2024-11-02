package com.ssafyhome.model.dto.house;

import lombok.Data;

@Data
public class HouseDto {

	private String aptSeq;
	private String aptNm;
	private String jibun;
	private String roadNm;
	private String latitude;
	private String longitude;
	private String AvgDealAmount;
	private double reviewRate;
	private boolean isBookmark;
	private int dealCnt;
	private double avgDealAmount;
}
