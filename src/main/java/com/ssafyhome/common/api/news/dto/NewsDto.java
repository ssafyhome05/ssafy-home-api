package com.ssafyhome.common.api.news.dto;

import java.util.List;
import lombok.Data;

@Data
public class NewsDto {
	
	private List<News> news;
	
	@Data
	public static class News {
		private String dongCode;
		private String title;
		private String url;
		private String cityName;
	}

}
