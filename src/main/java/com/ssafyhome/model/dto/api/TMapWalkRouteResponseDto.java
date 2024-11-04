package com.ssafyhome.model.dto.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TMapWalkRouteResponseDto extends TMapResponse<TMapWalkRouteResponseDto.Feature> {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Feature extends TMapResponse.Feature<TMapWalkRouteResponseDto.Feature.Geometry, TMapWalkRouteResponseDto.Feature.Properties> {

        @EqualsAndHashCode(callSuper = true)
        @Data
        public static class Geometry extends TMapResponse.Feature.Geometry {}

        @EqualsAndHashCode(callSuper = true)
        @Data
        public static class Properties extends TMapResponse.Feature.Properties {

            //COMMON
            private String facilityName;

            //POINT
            private String nearPoiName;
            private String nearPoiX;
            private String nearPoiY;
            private String intersectionName;

            //LINE
            private int categoryRoadType;
        }
    }
}
