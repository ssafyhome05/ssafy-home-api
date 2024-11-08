package com.ssafyhome.house.dto;

import com.ssafyhome.navigate.dto.NavigateDto;
import com.ssafyhome.review.dto.ReviewDto;
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
  private List<HouseDealDto> deals;
  private List<ReviewDto> reviews;
  private Map<Object, NavigateDto> navigates;
  private HouseGraphDto graph;
}
