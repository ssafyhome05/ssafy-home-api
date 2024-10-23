package com.ssafyhome.model.dto.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TMapTransportRouteRequestDto {

	private double startX;
	private double startY;
	private double endX;
	private double endY;
	private int count;
}
