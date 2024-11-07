package com.ssafyhome.api.tmap.dto;

import lombok.Data;

import java.util.List;

@Data
public abstract class TMapResponse<F extends TMapResponse.Feature> {

    private String type;
    private List<F> features;

    @Data
    public static class Feature <G extends TMapResponse.Feature.Geometry, P extends TMapResponse.Feature.Properties> {

        private String type;
        private G geometry;
        private P properties;

        @Data
        public static class Geometry {

            private String type;
            private List<Object> coordinates;
        }

        @Data
        public static class Properties {

            private int totalDistance;
            private int totalTime;

            private int index;
            private int pointIndex;
            private int lineIndex;

            private String name;
            private String description;
            private String direction;
            private int time;

            private int turnType;
            private int roadType;
            private String facilityType;
            private String pointType;
        }
    }
}
