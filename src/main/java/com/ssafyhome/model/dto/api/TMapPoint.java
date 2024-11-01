package com.ssafyhome.model.dto.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TMapPoint {

    private String name;
    private double x;
    private double y;
}
