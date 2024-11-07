package com.ssafyhome.common.api.gonggong.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Data;

import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class GonggongAptTradeResponse {

	private Header header;
	private Body body;

	@Data
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Header {

		private String resultCode;
		private String resultMsg;
	}

	@Data
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Body {

		private List<Item> items;
		private int numOfRows;
		private int pageNo;
		private int totalCount;
	}

	@Data
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Item {

		private String aptDong;
		private String aptNm;
		private String aptSeq;
		private Integer buildYear;

		private String dealAmount;
		private Integer dealYear;
		private Integer dealMonth;
		private Integer dealDay;
		private String dealingGbn;
		private String cdealType;
		private String cdealDay;
		private String buyerGbn;
		private String slerGbn;

		private String bonbun;
		private String bubun;
		private String jibun;
		private String umdCd;
		private String umdNm;
		private String sggCd;
		private String roadNm;
		private String roadNmBonbun;
		private String roadNmBubun;
		private String roadNmCd;
		private String roadNmSeq;
		private String roadNmSggCd;
		private String roadNmbCd;

		private Double excluUseAr;
		private String floor;
		private String landCd;
		private String landLeaseholdGbn;
		private String rgstDate;
		private String estateAgentSggNm;
	}

	private CmmMsgHeader cmmMsgHeader;

	@Data
	public static class CmmMsgHeader {

		private String errMsg;
		private String returnAuthMsg;
		private String returnReasonCode;
	}
}