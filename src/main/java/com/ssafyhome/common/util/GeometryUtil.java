package com.ssafyhome.common.util;

import com.ssafyhome.common.mapper.GeometryMapper;
import com.ssafyhome.common.entity.GeometryEntity;
import com.ssafyhome.common.util.object.Circle;
import com.ssafyhome.common.util.object.Point;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class GeometryUtil {

    private final GeometryMapper geometryMapper;
    private final CalcUtil calcUtil;

    public GeometryUtil(
            GeometryMapper geometryMapper,
            CalcUtil calcUtil
    ) {

        this.geometryMapper = geometryMapper;
        this.calcUtil = calcUtil;
    }

    public void updateCenterCircle(String dongCode) {

        GeometryEntity geometry = geometryMapper.selectByDongCode(dongCode);
        Circle circle = calcUtil.getMinimumBoundedCircle(getPoints(geometry));

        geometry.setRadius(circle.getRadius());
        geometry.setCenterLat(circle.getCenter().getLat());
        geometry.setCenterLng(circle.getCenter().getLng());

        geometryMapper.update(geometry);
    }

    public List<Point> getPoints(GeometryEntity geometry) {

        return convertWKB(geometry.getGeom());
    }

    private List<Point> convertWKB(byte[] bytes) {

        WKBReader wkbReader = new WKBReader();

        Geometry geometry = null;
        try {
            geometry = wkbReader.read(bytes);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return Arrays.stream(geometry.getCoordinates())
                .map(coordinate -> new Point(coordinate.getY(), coordinate.getX()))
                .toList();
    }
}
