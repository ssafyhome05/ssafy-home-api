package com.ssafyhome.model.entity.mysql;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewEntity {

  private String aptSeq;
  private long userSeq;
  private String reviewTitle;
  private int reviewRate;
  private String reviewContent;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
}
