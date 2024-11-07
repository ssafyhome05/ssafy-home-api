package com.ssafyhome.api.tmap.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TMapCarRouteRequestDto {

	private double startX;
	private double startY;
	private double endX;
	private double endY;
}
