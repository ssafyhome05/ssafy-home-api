package com.ssafyhome.model.dto;

import lombok.Data;

import java.util.Map;

@Data
public class BookmarkStatusDto {

	//애매함
	private Map<LocationDto, HouseStatusDto> locationStatuses;
	private Map<HouseDto, NavigateDto> houseStatuses;
	private Map<CustomSpotDto, NavigateDto> customSpotStatuses;
}
