package com.ssafyhome.spot.service;

import com.ssafyhome.spot.dto.SpotDto;

import java.util.List;
import java.util.Map;

public interface SpotService {

    Map<String, List<SpotDto>> getSpotsByDongCode(String dongCode);
}
