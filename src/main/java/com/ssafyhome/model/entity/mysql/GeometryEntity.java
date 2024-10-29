package com.ssafyhome.model.entity.mysql;

import lombok.Data;

@Data
public class GeometryEntity {

    private String dongCode;
    private String dongName;
    private byte[] geom;
    private double centerLng;
    private double centerLat;
    private double radius;
}
