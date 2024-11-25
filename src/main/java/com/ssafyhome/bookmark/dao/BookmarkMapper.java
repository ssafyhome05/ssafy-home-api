package com.ssafyhome.bookmark.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafyhome.house.dto.HouseDto;
import com.ssafyhome.spot.dto.CustomSpotDto;
import com.ssafyhome.spot.dto.LocationDto;

@Mapper
public interface BookmarkMapper {
	
    void addHouseBookmark(Map<String, Object> params);
    void addLocationBookmark(Map<String, Object> params);
    void addCustomSpotBookmark(Map<String, Object> params);

    List<HouseDto> getHouseList(String userSeq);
    List<LocationDto> getLocationList(String userSeq);
    List<CustomSpotDto> getCustomSpotList(String userSeq);
    
    void deleteHouseBookmark(Map<String, Object> params);
    void deleteLocationBookmark(Map<String, Object> params);
    void deleteCustomSpotBookmark(Map<String, Object> params);
}
