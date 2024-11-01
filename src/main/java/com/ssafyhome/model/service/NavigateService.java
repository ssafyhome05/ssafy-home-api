package com.ssafyhome.model.service;

import com.ssafyhome.model.dto.api.TMapPoint;
import com.ssafyhome.model.dto.navigate.NavigateDto;

import java.util.List;

public interface NavigateService {

    List<NavigateDto> getNavigates(String type, String aptSeq);
    NavigateDto getNavigate(String type, String aptSeq, TMapPoint end);
}
