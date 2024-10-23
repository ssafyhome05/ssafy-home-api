package com.ssafyhome.model.dto.bookmark;

import com.ssafyhome.model.dto.navigate.NavigateDto;
import com.ssafyhome.model.dto.house.HouseDto;
import com.ssafyhome.model.dto.spot.CustomSpotDto;
import com.ssafyhome.model.dto.spot.LocationDto;
import com.ssafyhome.model.dto.spot.LocationStatusDto;
import lombok.Data;

import java.util.Map;

@Data
public class BookmarkStatusDto {

	//애매함
	private Map<LocationDto, LocationStatusDto> locationStatuses;
	private Map<HouseDto, NavigateDto> houseStatuses;
	private Map<CustomSpotDto, NavigateDto> customSpotStatuses;
}
