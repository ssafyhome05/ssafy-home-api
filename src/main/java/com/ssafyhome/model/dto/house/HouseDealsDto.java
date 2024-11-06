package com.ssafyhome.model.dto.house;

import lombok.Data;

@Data
public class HouseDealsDto {

  private String dealSeq;
  private String aptDong;
  private String floor;
  private String dealYear;
  private String dealMonth;
  private String dealDay;
  private double excluUseAr;
  private String dealAmount;
}
