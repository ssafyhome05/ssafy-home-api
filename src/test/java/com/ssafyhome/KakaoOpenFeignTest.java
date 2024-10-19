package com.ssafyhome;

import com.ssafyhome.api.KakaoClient;
import com.ssafyhome.model.dto.KakaoKeywordPlaceDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("kakao openfeign test")
public class KakaoOpenFeignTest {

	@Value("${kakao.API-KEY}")
	private String apiKey;

	private final KakaoClient kakaoClient;

	@Autowired
	public KakaoOpenFeignTest(KakaoClient kakaoClient) {

		this.kakaoClient = kakaoClient;
	}

	@Test
	public void testKakaoKeywordPlace() {
		KakaoKeywordPlaceDto kakaoKeywordPlaceDto = kakaoClient.searchKeywordPlace("KakaoAK " + apiKey, "다이소");
		System.out.println(kakaoKeywordPlaceDto);
	}
}
