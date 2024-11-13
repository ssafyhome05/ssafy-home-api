package com.ssafyhome.bookmark.service;

import java.util.List;
import java.util.Map;

import com.ssafyhome.house.dto.HouseDto;
import com.ssafyhome.spot.dto.CustomSpotDto;
import com.ssafyhome.spot.dto.LocationDto;

public interface BookmarkService {
	
    void addHouseBookmark(Map<String, Object> params);
    void addLocationBookmark(Map<String, Object> params);
    void addCustomSpotBookmark(Map<String, Object> params);

    List<HouseDto> getHouseList();
    List<LocationDto> getLocationList();
    List<CustomSpotDto> getCustomSpotList();
    
    
    void deleteHouseBookmark(Map<String, Object> params);
    void deleteLocationBookmark(Map<String, Object> params);
    void deleteCustomSpotBookmark(Map<String, Object> params);

}
