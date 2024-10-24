package com.ssafyhome.model.service.impl;

import com.ssafyhome.model.dao.mapper.HouseMapper;
import com.ssafyhome.model.dto.house.HouseDealsDto;
import com.ssafyhome.model.dto.house.HouseDto;
import com.ssafyhome.model.service.HouseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseServiceImpl implements HouseService {
    private final HouseMapper houseMapper;

    public HouseServiceImpl(HouseMapper houseMapper) {
        this.houseMapper = houseMapper;
    }

    @Override
    public List<HouseDto> getHouseInfo(String dongCode) {

        List<HouseDto> houseInfoList = houseMapper.getHouseInfo(dongCode);

        return houseInfoList;
    }

    @Override
    public List<HouseDealsDto> getHouseDeals(String houseSeq, int page, int limit) {

        int offset = page * limit;
        List<HouseDealsDto> houseDealsList = houseMapper.getHouseDeals(houseSeq, limit, offset);

        return houseDealsList;
    }
}
