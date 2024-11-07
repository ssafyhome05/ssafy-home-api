package com.ssafyhome.navigate.service;

import com.ssafyhome.common.api.tmap.dto.TMapPoint;
import com.ssafyhome.navigate.dto.NavigateDto;
import com.ssafyhome.spot.dto.SpotSearchDto;

import java.util.List;

public interface NavigateService {

    List<NavigateDto> getNavigates(String type, String aptSeq);
    NavigateDto getNavigate(String type, String aptSeq, TMapPoint end);
    TMapPoint getEndPoint(SpotSearchDto spotSearchDto);
}
