package com.ssafyhome.model.service.impl;

import com.ssafyhome.model.dto.spot.SpotDto;
import com.ssafyhome.model.service.SpotService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SpotServiceImpl implements SpotService {

    @Override
    public Map<String, List<SpotDto>> getSpotsByDongCode(String dongCode) {
        return Map.of();
    }
}
