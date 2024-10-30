package com.ssafyhome.model.entity.mysql;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpotEntity {

	private String spotSeq;
	private String spotName;
	private String spotType;
	private String sggCd;
	private String umdCd;
	private String umdNm;
	private String jibun;
	private String roadNm;
//	private String roadNmSggCd;
//	private String roadNmBonbun;
//	private String roadNmBubun;
	private String latitude;
	private String longitude;
}
