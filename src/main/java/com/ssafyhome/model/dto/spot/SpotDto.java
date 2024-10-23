package com.ssafyhome.model.dto.spot;

import lombok.Data;

@Data
public class SpotDto {

  private long spotSeq;
  private String spotName;
  private String spotType;
  private String jibun;
  private String roadName;
  private String latitude;
  private String longitude;
}
