package com.ssafyhome.model.dto.house;

import lombok.Data;

@Data
public class HouseDto {

	private String aptSeq;
	private String aptName;
	private String jibun;
	private String roadName;
	private String latitude;
	private String longitude;
	private String AvgDealAmount;
	private double reviewRate;
	private boolean isBookmark;
}
