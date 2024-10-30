package com.ssafyhome.model.service;

import com.ssafyhome.model.dto.spot.SpotDto;

import java.util.List;
import java.util.Map;

public interface SpotService {

    Map<String, List<SpotDto>> getSpotsByDongCode(String dongCode);
}
