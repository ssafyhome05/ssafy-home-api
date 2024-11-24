package com.ssafyhome.house.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

@Data
@RedisHash(value = "search_keyword", timeToLive = 25 * 60 * 60)
public class SearchKeywordEntity {

  @Id
  private String id;

  private String keyword;
  private LocalDateTime searchedAt;

  public SearchKeywordEntity(String keyword, LocalDateTime searchedAt, String clientIp) {
    this.id = keyword + "_" + searchedAt.withMinute((searchedAt.getMinute() / 10) * 10).truncatedTo(ChronoUnit.MINUTES) + "_" + clientIp;
    this.keyword = keyword;
    this.searchedAt = searchedAt;
  }
}
