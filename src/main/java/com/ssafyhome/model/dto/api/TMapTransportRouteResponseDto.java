package com.ssafyhome.model.dto.api;

import lombok.Data;

import java.util.List;

@Data
public class TMapTransportRouteResponseDto {

	private MetaData metaData;

	@Data
	public static class MetaData {

		private Plan plan;
		private RequestParameters requestParameters;

		@Data
		public static class Plan {

			private List<Itinerary> itineraries;

			@Data
			public static class Itinerary {

				private int totalTime;
				private int transferCount;
				private int totalWalkDistance;
				private int totalDistance;
				private int totalWalkTime;
				private Fare fare;
				private List<Leg> legs;

				@Data
				public static class Fare {

					private Regular regular;

					@Data
					public static class Regular {

						private int totalFare;
						private Currency currency;

						@Data
						public static class Currency {

							private String symbol;
							private String currency;
							private String currencyCode;
						}
					}
				}

				@Data
				public static class Leg {

					private int distance;
					private int sectionTime;
					private String mode;
					private String route;
					private String routeColor;
					private String routeId;
					private String type;
					private int routePayment;
					private int service;
					private Lane lane;
					private Point start;
					private Point end;
					private List<Step> steps;
					private PassStopList passStopList;
					private PassShape passShape;

					@Data
					public static class Lane {

						private String routeColor;
						private String routeId;
						private String route;
						private int service;
						private int type;
					}

					@Data
					public static class Point {

						private double lon;
						private double lat;
						private String name;
					}

					@Data
					public static class Step {

						private double distance;
						private String streetName;
						private String description;
						private String linestring;
					}

					@Data
					public static class PassStopList {

						private List<Station> stationList;
						private List<Station> stations;

						@Data
						public static class Station {

							private int index;
							private String stationId;
							private String stationName;
							private String lon;
							private String lat;
							private int pathType;
						}
					}

					@Data
					public static class PassShape {

						private String linestring;
					}
				}
			}
		}

		@Data
		public static class RequestParameters {

			private String reqDttm;
			private String startX;
			private String startY;
			private String endX;
			private String endY;
			private String locale;
			private int busCount;
			private int subwayCount;
			private int expressbusCount;
			private int trainCount;
			private int airplaneCount;
			private int ferryCount;
			private int wideareaRouteCount;
		}
	}
}
