package com.ssafyhome.common.api.sgis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SgisSearchPopulation {

  private String id;
  @JsonProperty("errMsg")
  private String errMsg;
  @JsonProperty("errCd")
  private String errCd;
  @JsonProperty("trId")
  private String trId;
  private List<Result> result;

  @Data
  public static class Result {

    private String admCd;
    private String admNm;
    private String avgAge;
    private int population;
  }
}
