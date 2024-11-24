package com.ssafyhome.bookmark.service;

import com.ssafyhome.bookmark.dao.BookmarkMapper;
import com.ssafyhome.house.dto.HouseDto;
import com.ssafyhome.spot.dto.CustomSpotDto;
import com.ssafyhome.spot.dto.LocationDto;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkMapper bookmarkMapper;

    public BookmarkServiceImpl(
            BookmarkMapper bookmarkMapper
    ) {
        this.bookmarkMapper = bookmarkMapper;
    }

    @Override
    public void addHouseBookmark(Map<String, Object> params) {
        bookmarkMapper.addHouseBookmark(params);
    }

    @Override
    public void deleteHouseBookmark(Map<String, Object> params) {
        bookmarkMapper.deleteHouseBookmark(params);
    }

    @Override
    public void addLocationBookmark(Map<String, Object> params) {
        bookmarkMapper.addLocationBookmark(params);
    }

    @Override
    public void deleteLocationBookmark(Map<String, Object> params) {
        bookmarkMapper.deleteLocationBookmark(params);
    }

	@Override
	public void addCustomSpotBookmark(Map<String, Object> params) {
		bookmarkMapper.addCustomSpotBookmark(params);
		
	}

	@Override
	public List<HouseDto> getHouseList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return bookmarkMapper.getHouseList(params);
	}

	@Override
	public List<LocationDto> getLocationList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return bookmarkMapper.getLocationList(params);
	}

	@Override
	public List<CustomSpotDto> getCustomSpotList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return bookmarkMapper.getCustomSpotList(params);
	}

	@Override
	public void deleteCustomSpotBookmark(Map<String, Object> params) {
		bookmarkMapper.deleteCustomSpotBookmark(params);
		
	}

}
