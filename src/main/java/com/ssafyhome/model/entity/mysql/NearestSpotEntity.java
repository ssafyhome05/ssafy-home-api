package com.ssafyhome.model.entity.mysql;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NearestSpotEntity {

    private String aptSeq;
    private String categoryName;
    private String spotName;
    private String longitude;
    private String latitude;
}
