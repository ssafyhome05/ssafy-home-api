package com.ssafyhome;

import com.ssafyhome.common.mapper.GeometryMapper;
import com.ssafyhome.common.util.GeometryUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GeometryTest {

    private final GeometryUtil geometryUtil;
    private final GeometryMapper geometryMapper;

    @Autowired
    public GeometryTest(
            GeometryUtil geometryUtil,
            GeometryMapper geometryMapper
    ) {

        this.geometryUtil = geometryUtil;
        this.geometryMapper = geometryMapper;
    }

    @Test
    public void updateCircleTest() {

        geometryUtil.updateCenterCircle("1111010100");
        System.out.println(geometryUtil.getPoints(geometryMapper.selectByDongCode("1111010100")));
    }
}
