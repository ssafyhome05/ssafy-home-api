package com.ssafyhome.house.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Data
@RedisHash(value = "search_keyword", timeToLive = 25 * 60 * 60)
public class SearchKeywordEntity {

  @Id
  private String id;

  private String keyword;
  private LocalDateTime searchedAt;

  public SearchKeywordEntity(String keyword, LocalDateTime searchedAt) {
    this.id = keyword + "_" + searchedAt.toString();
    this.keyword = keyword;
    this.searchedAt = searchedAt;
  }
}
