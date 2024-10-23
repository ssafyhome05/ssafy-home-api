package com.ssafyhome.model.dto.api;

import lombok.Data;

import java.util.List;

@Data
public class TMapCarRouteResponseDto {

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
		private String traffic;
	}

	@Data
	public static class Properties {

		//SP
		private int totalDistance;
		private int totalTime;
		private int totalFare;
		private int taxiFare;

		private int index;
		private int pointIndex;
		private int lineIndex;
		private int departIdx;
		private int destIdx;

		private String name;
		private String nextRoadName;
		private String description;
		private String direction;
		private int time;
		private int distance;

		private int turnType;
		private int roadType;
		private String facilityType;
		private String pointType;
	}
}
