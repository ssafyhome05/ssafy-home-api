package com.ssafyhome.house.dto;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class SearchPopulationDto {

  private AtomicInteger ageUnder20Population;
  private AtomicInteger age2030Population;
  private AtomicInteger age4060Population;
  private AtomicInteger ageOver70Population;

  public SearchPopulationDto() {

    this.ageUnder20Population = new AtomicInteger();
    this.age2030Population = new AtomicInteger();
    this.age4060Population = new AtomicInteger();
    this.ageOver70Population = new AtomicInteger();
  }

  public void setPopulationByAge(String generation, int population) {
    switch (generation) {
      case "UNDER_20" -> this.ageUnder20Population.addAndGet(population);
      case "2030" -> this.age2030Population.addAndGet(population);
      case "4060" -> this.age4060Population.addAndGet(population);
      case "OVER_70" -> this.ageOver70Population.addAndGet(population);
    }
  }
}
