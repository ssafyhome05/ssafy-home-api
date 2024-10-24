package com.ssafyhome.model.service;

import com.ssafyhome.model.dto.house.HouseDto;

import java.util.List;

public interface HouseService {
    List<HouseDto> getHouseInfo(String dongCode);
}
