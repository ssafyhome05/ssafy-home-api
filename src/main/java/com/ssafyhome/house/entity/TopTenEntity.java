package com.ssafyhome.house.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@RedisHash(value = "top_ten", timeToLive = 60 * 60)
public class TopTenEntity {

  @Id
  private String rankTime;
  private Map<String, Element> elements;

  @Data
  public static class Element implements Serializable {

    private String keyword;
    private int rank;
    private int changed;

    public Element(String keyword, int rank) {

      this.keyword = keyword;
      this.rank = rank;
    }
  }
}