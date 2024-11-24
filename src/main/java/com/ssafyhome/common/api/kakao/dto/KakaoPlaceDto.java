package com.ssafyhome.common.api.kakao.dto;

import lombok.Data;

import java.util.List;

@Data
public class KakaoPlaceDto {

	private Meta meta;
	private List<Document> documents;

	@Data
	public static class Meta {

		private int totalCount;
		private int pageableCount;
		private boolean isEnd;
		private SameName sameName;

		@Data
		public static class SameName {

			private String[] region;
			private String keyword;
			private String selectedRegion;
		}
	}

	@Data
	public static class Document {

		private String placeName;
		private String distance;
		private String placeUrl;
		private String categoryName;
		private String addressName;
		private String roadAddressName;
		private String id;
		private String phone;
		private String categoryGroupCode;
		private String categoryGroupName;
		private String x;
		private String y;
	}

}
