package com.ssafyhome.model.entity.mysql;

import lombok.Data;

@Data
public class SpotEntity {

	private String spotSeq;
	private String spotName;
	private String spotType;
	private String sggCd;
	private String umdCd;
	private String umdNm;
	private String jibun;
	private String roadNmSggCd;
	private String roadNm;
	private String roadNmBonbun;
	private String roadNmBubun;
	private String latitude;
	private String longitude;
}
