package com.ssafyhome;

import com.ssafyhome.api.gonggong.GonggongClient;
import com.ssafyhome.api.kakao.KakaoClient;
import com.ssafyhome.api.sgis.SGISClient;
import com.ssafyhome.api.sgis.SGISUtil;
import com.ssafyhome.api.tmap.TMapClient;
import com.ssafyhome.model.dto.api.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OpenFeignTest {

    private final KakaoClient kakaoClient;
    private final SGISClient sgisClient;
    private final SGISUtil sgisUtil;
    private final TMapClient tMapClient;
    private final GonggongClient gonggongClient;

    @Autowired
    public OpenFeignTest(
            KakaoClient kakaoClient,
            SGISClient sgisClient,
            SGISUtil sgisUtil,
            TMapClient tMapClient,
            GonggongClient gonggongClient
    ) {

        this.kakaoClient = kakaoClient;
        this.sgisClient = sgisClient;
        this.sgisUtil = sgisUtil;
        this.tMapClient = tMapClient;
        this.gonggongClient = gonggongClient;
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

    @Test
    public void testAccessToken() {

        System.out.println(sgisUtil.getAccessToken());
    }

    @Test
    public void convertAddressToGeoJson(){

        SgisGeoCode sgisGeoCode = sgisClient.getGeocode(
                sgisUtil.getAccessToken(),
                "역삼동 718-5"
        );

        System.out.println(sgisGeoCode);
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

    @Test
    public void gonggongAptTradeTest() {

        GonggongAptTradeResponse gonggongAptTradeResponse = gonggongClient.getRTMSDataSvcAptTradeDev(
            11110,
            202111,
            "zkPCGLLeYNp7DcQ1XOEYpbo80W8vTNv7cJs5z8VNP8CRbAbI7KMwWdAgBRmHJ/UVxEKnC2F6qi8jjnn9148NgA==",
            1,
            10
        );

        System.out.println(gonggongAptTradeResponse);
    }
}
