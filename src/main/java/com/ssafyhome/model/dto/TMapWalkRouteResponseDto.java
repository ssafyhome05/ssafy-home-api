package com.ssafyhome.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class TMapWalkRouteResponseDto {

    private String type;
    private List<Feature> features;

    @Data
    public static class Feature {

        private String type;
        private Geometry geometry;
        private Properties properties;
    }

    @Data
    public static class Geometry {

        private String type;
        private List<Object> coordinates;
    }

    @Data
    public static class Properties {

        //COMMON
        private int index;
        private String name;
        private String description;
        private String facilityType;
        private String facilityName;

        //SP
        private int totalDistance;
        private int totalTime;

        //POINT
        private int pointIndex;
        private String direction;
        private String nearPoiName;
        private String nearPoiX;
        private String nearPoiY;
        private String intersectionName;
        private int turnType;
        private String pointType;

        //LINE
        private int lineIndex;
        private int time;
        private int roadType;
        private int categoryRoadType;
    }
}
