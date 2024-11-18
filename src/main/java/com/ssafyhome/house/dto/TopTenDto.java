package com.ssafyhome.house.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TopTenDto {

  private String rankTime;
  private List<Element> elements;

  public TopTenDto(String rankTime) {

    this.rankTime = rankTime;
  }

  @Data
  public static class Element {

    private String keyword;
    private String dongName;
    private int rank;
    private int changed;
  }
}
