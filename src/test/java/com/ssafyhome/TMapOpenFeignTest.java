package com.ssafyhome;

import com.ssafyhome.api.TMapClient;
import com.ssafyhome.model.dto.TMapWalkRouteRequestDto;
import com.ssafyhome.model.dto.TMapWalkRouteResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("t-map openfeign test")
public class TMapOpenFeignTest {

    @Value("${t-map.API_KEY}")
    private String apiKey;

    private final TMapClient tMapClient;

    @Autowired
    public TMapOpenFeignTest(TMapClient tMapClient) {

        this.tMapClient = tMapClient;
    }

    @Test
    public void tMapWalkTest() {

        TMapWalkRouteRequestDto requestDto = TMapWalkRouteRequestDto.builder()
                .startX(126.92365493654832)
                .startY(37.556770374096615)
                .endX(126.92432158129688)
                .endY(37.55279861528311)
                .startName("%EC%B6%9C%EB%B0%9C")
                .endName("%EB%8F%84%EC%B0%A9")
                .build();

        TMapWalkRouteResponseDto responseDto = tMapClient.findWalkRoute(
                apiKey,
                1,
                requestDto
        );

        System.out.println(responseDto);
    }
}
