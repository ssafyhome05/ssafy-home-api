package com.ssafyhome.model.entity.mysql;

import lombok.Data;

import java.time.Instant;

@Data
public class ReviewEntity {
  private int seq;
  private String title;
  private String content;
  private String author;
  private Instant createdAt;

}
