package com.ssafyhome.house.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PopulationDto {

  private String dongCode;
  private int totalPopulation;
  private String populationDensity;
  private int corpCnt;
  private int houseCnt;
  private List<Generation> generations;

  @Data
  public static class Generation {

    private String generation;
    private int population;
    private String ratio;

    public Generation(String generation, int population, int totalPopulation) {

      this.generation = generation;
      this.population = population;
      this.ratio = String.format("%.2f", (double)population / totalPopulation * 100);
    }
  }
}
