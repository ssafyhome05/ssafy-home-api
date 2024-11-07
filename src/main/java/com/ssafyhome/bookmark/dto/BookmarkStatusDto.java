package com.ssafyhome.bookmark.dto;

import com.ssafyhome.navigate.dto.NavigateDto;
import com.ssafyhome.house.dto.HouseDto;
import com.ssafyhome.spot.dto.CustomSpotDto;
import com.ssafyhome.spot.dto.LocationDto;
import com.ssafyhome.spot.dto.LocationStatusDto;
import lombok.Data;

import java.util.Map;

@Data
public class BookmarkStatusDto {

	//애매함
	private Map<LocationDto, LocationStatusDto> locationStatuses;
	private Map<HouseDto, NavigateDto> houseStatuses;
	private Map<CustomSpotDto, NavigateDto> customSpotStatuses;
}
