package com.ssafyhome.model.dto.house;

import com.ssafyhome.model.dto.navigate.NavigateDto;
import com.ssafyhome.model.dto.review.ReviewDto;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class HouseDetailDto {

  private String aptSeq;
  private String aptName;
  private String jibun;
  private String roadName;
  private String latitude;
  private String longitude;
  private String AvgDealAmount;
  private double reviewRate;
  private boolean isBookmark;
  private List<HouseDealsDto> deals;
  private List<ReviewDto> reviews;
  private Map<Object, NavigateDto> navigates;
  private HouseGraphDto graph;
}
