package com.ssafyhome;

import com.ssafyhome.api.kakao.KakaoClient;
import com.ssafyhome.model.dto.KakaoPlaceDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("kakao openfeign test")
public class KakaoOpenFeignTest {

	private final KakaoClient kakaoClient;

	@Autowired
	public KakaoOpenFeignTest(KakaoClient kakaoClient) {

		this.kakaoClient = kakaoClient;
	}

	@Test
	public void testKakaoKeywordPlace() {

		KakaoPlaceDto kakaoPlaceDto = kakaoClient.searchKeywordPlace("다이소");
		System.out.println(kakaoPlaceDto);
	}

	@Test
	public void testKakaoCategoryPlace() {

		KakaoPlaceDto kakaoPlaceDto = kakaoClient.searchCategoryPlace("CS2");
		System.out.println(kakaoPlaceDto);
	}
}
