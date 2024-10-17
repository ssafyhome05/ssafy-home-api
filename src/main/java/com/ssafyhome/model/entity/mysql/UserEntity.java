package com.ssafyhome.model.entity.mysql;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserEntity {

  private long userSeq;
  private String userId;
  private String userPw;
  private String userEmail;
  private String userName;
  private String userPhone;
  private String userZipcode;
  private String userAddress;
  private String userAddress2;
  private String socialType;
  private String socialPlatform;
  private LocalDateTime createdAt;
}
