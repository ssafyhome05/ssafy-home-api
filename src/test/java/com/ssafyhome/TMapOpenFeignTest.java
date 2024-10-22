package com.ssafyhome;

import com.ssafyhome.api.tmap.TMapClient;
import com.ssafyhome.model.dto.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("t-map openfeign test")
public class TMapOpenFeignTest {

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

        TMapWalkRouteResponseDto responseDto = tMapClient.findWalkRoute(1, requestDto);

        System.out.println(responseDto);
    }

    @Test
    @Disabled
    public void tMapTransportTest() {

        TMapTransportRouteRequestDto requestDto = TMapTransportRouteRequestDto.builder()
            .startX(126.926493082645)
            .startY(37.6134436427887)
            .endX(127.126936754911)
            .endY(37.5004198786564)
            .count(10)
            .build();

        TMapTransportRouteResponseDto responseDto = tMapClient.findTransportRoute(requestDto);

        System.out.println(responseDto);
    }

    @Test
    public void tMapCarTest() {

        TMapCarRouteRequestDto requestDto = TMapCarRouteRequestDto.builder()
            .startX(126.926493082645)
            .startY(37.6134436427887)
            .endX(127.126936754911)
            .endY(37.5004198786564)
            .build();

        TMapCarRouteResponseDto responseDto = tMapClient.findCarRoute(1, requestDto);

        System.out.println(responseDto);
    }
}
