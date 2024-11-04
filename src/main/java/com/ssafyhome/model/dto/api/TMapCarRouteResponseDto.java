package com.ssafyhome.model.dto.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TMapCarRouteResponseDto extends TMapResponse<TMapCarRouteResponseDto.Feature>{

	@EqualsAndHashCode(callSuper = true)
	@Data
	public static class Feature extends TMapResponse.Feature<TMapCarRouteResponseDto.Feature.Geometry, TMapCarRouteResponseDto.Feature.Properties> {

		@EqualsAndHashCode(callSuper = true)
		@Data
		public static class Geometry extends TMapResponse.Feature.Geometry {

			private String traffic;
		}

		@EqualsAndHashCode(callSuper = true)
		@Data
		public static class Properties extends TMapResponse.Feature.Properties {

			private int totalFare;
			private int taxiFare;

			private int departIdx;
			private int destIdx;

			private String nextRoadName;
			private int distance;
		}
	}
}
