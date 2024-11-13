package com.ssafyhome.house.dto;

import lombok.Data;

@Data
public class HouseGraphDto {
    private int month;
    private int dealCnt;
    private double avgDealAmount;
    private double avgAreaPrice;
}
