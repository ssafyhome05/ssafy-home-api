package com.ssafyhome.model.dto.house;

import lombok.Data;

@Data
public class HouseDealsDto {

  private long dealSeq;
  private String aptDong;
  private String floor;
  private String dealYear;
  private String dealMonth;
  private String dealDay;
  private double exclusiveUserAr;
  private String dealAmount;
}
