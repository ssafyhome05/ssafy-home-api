package com.ssafyhome.common.api.news;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.ssafyhome.common.api.news.dto.NewsDto;
import com.ssafyhome.common.api.sgis.FeignSnakeCaseConfig;

@FeignClient(
		name = "Zipchack",
		url="127.0.0.1:8000",
		configuration = {
				FeignSnakeCaseConfig.class,
		}
)
public interface NewsClient {
	
	@GetMapping("/news")
	NewsDto getNews(); 
	

}
