package com.ssafyhome.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TMapWalkRouteRequestDto {

	private String startName;
	private double startX;
	private double startY;
	private String endName;
	private double endX;
	private double endY;
	
}
