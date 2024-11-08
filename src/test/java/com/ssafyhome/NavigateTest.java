package com.ssafyhome;

import com.ssafyhome.common.api.tmap.dto.TMapPoint;
import com.ssafyhome.navigate.service.NavigateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NavigateTest {

    private final NavigateService navigateService;

    @Autowired
    public NavigateTest(NavigateService navigateService) {

        this.navigateService = navigateService;
    }

    @Test
    public void navigateTest() {

        TMapPoint end = TMapPoint.builder()
                .name("홍제역.서대문세무서")
                .x(126.94413611111)
                .y(37.588652777777)
                .build();
        System.out.println(navigateService.getNavigate("search", "11110-101", end));
    }

    @Test
    public void navigateTest2() {

        System.out.println(navigateService.getNavigateList("spot", "11110-101"));
    }
}
