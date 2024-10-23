package com.ssafyhome.model.dto.spot;

import lombok.Data;

@Data
public class LocationStatusDto {

  private LocationDto location;
  private String avgArAmount;
  private String avgAge;
  private boolean isBookmark;
}
