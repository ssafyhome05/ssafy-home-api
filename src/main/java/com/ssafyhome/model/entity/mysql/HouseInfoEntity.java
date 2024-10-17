package com.ssafyhome.model.entity.mysql;

import lombok.Data;

@Data
public class HouseInfoEntity {

	private String houseSeq;
	private String sggCd;
	private String umdCd;
	private String umdNm;
	private String jibun;
	private String roadNmSggCd;
	private String roadNm;
	private String roadNmBonbun;
	private String roadNmBubun;
	private String aptNm;
	private int buildYear;
	private String latitude;
	private String longitude;
}
