package com.ssafyhome.spot.service;

import com.ssafyhome.spot.dao.SpotMapper;
import com.ssafyhome.spot.dto.SpotDto;
import com.ssafyhome.spot.entity.CategoryEntity;
import com.ssafyhome.spot.entity.SpotEntity;
import com.ssafyhome.util.ConvertUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpotServiceImpl implements SpotService {

    private final SpotInternalService spotInternalService;
    private final SpotMapper spotMapper;
    private final ConvertUtil convertUtil;

    public SpotServiceImpl(
            SpotInternalService spotInternalService,
            SpotMapper spotMapper,
            ConvertUtil convertUtil
    ) {

        this.spotInternalService = spotInternalService;
        this.spotMapper = spotMapper;
        this.convertUtil = convertUtil;
    }

    @Override
    public Map<String, List<SpotDto>> getSpotsByDongCode(String dongCode) {

        Map<String, List<SpotDto>> spotDtoMap = new HashMap<>();
        try {
            spotInternalService.getSpotsFromAPI(dongCode);

            for (CategoryEntity category : spotMapper.getCategories()) {
                List<SpotEntity> spotEntityList = spotMapper.getSpotsByCategory(dongCode, category.getCategoryName());
                List<SpotDto> spotDtoList = convertUtil.convert(spotEntityList, SpotDto.class);
                spotDtoMap.put(category.getCategoryName(), spotDtoList);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return spotDtoMap;
    }
}
