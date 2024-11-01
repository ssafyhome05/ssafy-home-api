package com.ssafyhome;

import com.ssafyhome.model.dto.api.TMapPoint;
import com.ssafyhome.model.service.NavigateService;
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

        TMapPoint start = TMapPoint.builder()
                .name("불광역3.6호선")
                .x(126.92822222222)
                .y(37.612805555556)
                .build();
        TMapPoint end = TMapPoint.builder()
                .name("홍제역.서대문세무서")
                .x(126.94413611111)
                .y(37.588652777777)
                .build();
        System.out.println(navigateService.getNavigate("search", "11110-101", end));
    }

    @Test
    public void navigateTest2() {

        TMapPoint start = TMapPoint.builder()
                .name("크래스빌")
                .x(126.97421071959718)
                .y(37.608527021517375)
                .build();
        System.out.println(navigateService.getNavigates("spot", "11110-101"));
    }
}
