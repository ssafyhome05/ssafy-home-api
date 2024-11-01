package com.ssafyhome.model.dto.navigate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class NavigateDto {

    private RequestParameter requestParameter;
    private Map<String, List<Route>> routes;

    @Data
    @Builder
    public static class RequestParameter {

        private String endPointName;
        private String endType;
    }

    @Data
    public static class Route {

        private int totalTime;
        private int totalDistance;
        private int fare;
        private List<RouteInfo> routeInfos;

        @Data
        @Builder
        public static class RouteInfo {

            private String type;
            private List<Object> coordinates;
            private TransferParameter transferParameter;

            @Data
            @Builder
            public static class TransferParameter {

                private String mode;
                private String color;
                private String name;
                private String id;
                private Point start;
                private Point end;

                @Data
                @Builder
                @AllArgsConstructor
                @NoArgsConstructor
                public static class Point {

                    private String name;
                    private double lon;
                    private double lat;
                }
            }
        }
    }
}
