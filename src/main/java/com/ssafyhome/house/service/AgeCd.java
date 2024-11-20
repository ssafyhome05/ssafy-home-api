package com.ssafyhome.house.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgeCd {
  UNDER_TEENAGER("30","UNDER_20"),
  TEENAGER("31","UNDER_20"),
  TWENTIES("32","2030"),
  THIRTIES("33","2030"),
  FORTIES("34","4060"),
  FIFTIES("35","4060"),
  SIXTIES("36","4060"),
  OVER_SEVENTIES("40","OVER_70");

  private final String code;
  private final String generation;
}